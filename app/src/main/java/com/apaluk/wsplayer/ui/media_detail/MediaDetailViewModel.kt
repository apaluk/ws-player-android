package com.apaluk.wsplayer.ui.media_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apaluk.wsplayer.core.navigation.WsPlayerNavArgs
import com.apaluk.wsplayer.core.util.withLeadingZeros
import com.apaluk.wsplayer.domain.model.media.MediaStream
import com.apaluk.wsplayer.domain.model.media.StreamsMediaType
import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import com.apaluk.wsplayer.domain.use_case.media.GetMediaDetailUiStateUseCase
import com.apaluk.wsplayer.domain.use_case.media.GetSeasonEpisodesUseCase
import com.apaluk.wsplayer.domain.use_case.media.GetStreamsUiStateUseCase
import com.apaluk.wsplayer.domain.use_case.media.UpdateWatchHistoryOnStartStreamUseCase
import com.apaluk.wsplayer.ui.common.util.toUiState
import com.apaluk.wsplayer.ui.media_detail.tv_show.TvShowPosterData
import com.apaluk.wsplayer.ui.media_detail.util.relativeProgress
import com.apaluk.wsplayer.ui.media_detail.util.tvShowUiState
import com.apaluk.wsplayer.ui.media_detail.util.updateTvShowUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getMediaDetailUiState: GetMediaDetailUiStateUseCase,
    getTvShowSeasonEpisodes: GetSeasonEpisodesUseCase,
    private val updateWatchHistoryOnStartStream: UpdateWatchHistoryOnStartStreamUseCase,
    getStreamsUiState: GetStreamsUiStateUseCase
): ViewModel() {

    private val mediaId: String = requireNotNull(savedStateHandle[WsPlayerNavArgs.MEDIA_ID_ARG])

    private val _uiState = MutableStateFlow(MediaDetailScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val selectedEpisode = combine(
        _uiState.mapNotNull { it.tvShowUiState?.selectedEpisodeIndex }.distinctUntilChanged(),
        _uiState.mapNotNull { it.tvShowUiState?.selectedSeasonEpisodes }.distinctUntilChanged()
    ) { index, episodes ->
        episodes.getOrNull(index)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val selectedSeason = combine(
        _uiState.mapNotNull { it.tvShowUiState?.selectedSeasonIndex }.distinctUntilChanged(),
        _uiState.mapNotNull { it.tvShowUiState?.seasons }.distinctUntilChanged()
    ) { index, seasons ->
        seasons.getOrNull(index)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val mediaIdForStreams: StateFlow<String> = selectedEpisode
        .filterNotNull()
        .map { it.id }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), mediaId)

    private val streamsMediaType: StreamsMediaType
    get() = if(selectedEpisode.value != null) StreamsMediaType.TvShowEpisode else StreamsMediaType.Movie

    init {
        // get media detail UI state
        viewModelScope.launch {
            getMediaDetailUiState(mediaId).collect { mediaDetailUiStateResource ->
                _uiState.update {
                    it.copy(
                        uiState = mediaDetailUiStateResource.toUiState(),
                        mediaDetailUiState = mediaDetailUiStateResource.data
                    )
                }
            }
        }
        // get streams
        viewModelScope.launch {
            mediaIdForStreams.collectLatest { mediaId ->
                getStreamsUiState(mediaId, streamsMediaType).collect { streamsUiState ->
                    _uiState.update {
                        it.copy(streamsUiState = streamsUiState)
                    }
                }
            }
        }
        // get episodes on season select
        viewModelScope.launch {
            selectedSeason.filterNotNull().collectLatest { season ->
                getTvShowSeasonEpisodes(mediaId, season.id).collect { seasonEpisodes ->
                    _uiState.updateTvShowUiState {
                        it.copy(
                            selectedSeasonEpisodes = seasonEpisodes.data?.episodes,
                            selectedEpisodeIndex = seasonEpisodes.data?.selectedEpisodeIndex,
                            tvShowEpisodesUiState = seasonEpisodes.toUiState()
                        )
                    }
                }
            }
        }
        // update poster data according to selected episode
        viewModelScope.launch {
            combine(
                selectedSeason.filterNotNull(),
                selectedEpisode.filterNotNull(),
                uiState.map { it.streamsUiState?.selectedStreamId }
            ) { season, episode, selectedStream ->
                TvShowPosterData(
                    episodeNumber = "S${season.seasonNumber.withLeadingZeros(2)}E${episode.episodeNumber.withLeadingZeros(2)}",
                    episodeName = episode.title,
                    duration = episode.duration,
                    imageUrl = episode.imageUrl ?: season.imageUrl,
                    progress = episode.relativeProgress ?: 0f,
                    showPlayButton = selectedStream != null,
                )
            }.collect { posterData ->
                _uiState.updateTvShowUiState { it.copy(posterData = posterData) }
            }
        }
    }

    fun onMediaDetailAction(action: MediaDetailAction) {
        when(action) {
            MediaDetailAction.PlayDefault -> onPlayDefault()
            is MediaDetailAction.PlayStream -> onPlayStream(action)
            is MediaDetailAction.SelectTvShowSeason -> onSelectTvShowSeason(action)
            is MediaDetailAction.SelectTvShowEpisode -> onSelectTvShowEpisode(action)
        }
    }

    private fun onPlayDefault() {
        _uiState.value.streamsUiState?.selectedStreamId?.let { streamIdent ->
            onPlayStream(MediaDetailAction.PlayStream(MediaStream(ident = streamIdent)))
        }
    }

    private fun onPlayStream(action: MediaDetailAction.PlayStream) {
        viewModelScope.launch {
            val watchHistoryId = updateWatchHistoryOnStartStream(
                stream = action.stream,
                mediaId = mediaId,
                season = selectedSeason.value?.id,
                episode = selectedEpisode.value?.id
            )
            _uiState.value.playStreamEvent.emit(
                PlayStreamParams(
                    ident = action.stream.ident,
                    watchHistoryId = watchHistoryId
                )
            )
        }
    }

    private fun onSelectTvShowSeason(action: MediaDetailAction.SelectTvShowSeason) {
        _uiState.updateTvShowUiState { it.copy(selectedSeasonIndex = action.seasonIndex) }
    }

    private fun onSelectTvShowEpisode(action: MediaDetailAction.SelectTvShowEpisode) {
        if(action.episodeIndex != _uiState.value.tvShowUiState?.selectedEpisodeIndex) {
            _uiState.update { it.copy(streamsUiState = null) }
            _uiState.updateTvShowUiState { it.copy(selectedEpisodeIndex = action.episodeIndex) }
        }
    }
}
