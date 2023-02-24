package com.apaluk.wsplayer.ui.media_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.core.navigation.WsPlayerNavArgs
import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.core.util.withLeadingZeros
import com.apaluk.wsplayer.domain.model.media.MediaDetailTvShow
import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import com.apaluk.wsplayer.domain.use_case.media.GetSelectedEpisodeUseCase
import com.apaluk.wsplayer.domain.use_case.media.GetSelectedSeasonUseCase
import com.apaluk.wsplayer.ui.common.util.UiState
import com.apaluk.wsplayer.ui.common.util.toUiState
import com.apaluk.wsplayer.ui.media_detail.tv_show.TvShowPosterData
import com.apaluk.wsplayer.ui.media_detail.util.toMediaDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailViewModel @Inject constructor(
    private val streamCinemaRepository: StreamCinemaRepository,
    getSelectedSeason: GetSelectedSeasonUseCase,
    getSelectedEpisode: GetSelectedEpisodeUseCase,
    savedStateHandle: SavedStateHandle
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

    init {
        // get media detail
        viewModelScope.launch {
            streamCinemaRepository.getMediaDetails(mediaId).collect { mediaDetailResource ->
                _uiState.update {
                    it.copy(
                        uiState = mediaDetailResource.toUiState(),
                        mediaDetailUiState = mediaDetailResource.data?.toMediaDetailUiState(),
                    )
                }
                // get TV show seasons
                if(mediaDetailResource is Resource.Success && mediaDetailResource.data is MediaDetailTvShow) {
                    _uiState.updateTvShowUiState {
                        it.copy(posterData = TvShowPosterData(imageUrl = mediaDetailResource.data.imageUrl))
                    }
                    val seasonsResource = streamCinemaRepository.getTvShowSeasons(mediaId).last()
                    if(seasonsResource is Resource.Success) {
                        seasonsResource.data?.let { seasons ->
                            val selectedSeasonIndex = getSelectedSeason(mediaId, seasons)
                            _uiState.updateTvShowUiState {
                                it.copy(
                                    seasons = seasons,
                                    selectedSeasonIndex = selectedSeasonIndex
                                )
                            }
                        }
                    } else if(seasonsResource is Resource.Error) {
                        _uiState.update { it.copy(uiState = seasonsResource.toUiState()) }
                    }
                }
            }
        }
        // get streams
        viewModelScope.launch {
            mediaIdForStreams.collect { mediaId ->
                val streamsResource = streamCinemaRepository.getMediaStreams(mediaId).last()
                _uiState.update {
                    it.copy(streams = streamsResource.data?.sortedBy { stream -> stream.size })
                }
            }
        }
        // get episodes on season select
        viewModelScope.launch {
            selectedSeason
                .filterNotNull()
                .collect { season ->
                    streamCinemaRepository
                        .getTvShowSeasonEpisodes(season.id)
                        .collect { episodesResource ->
                            val selectedEpisode = if (episodesResource.data != null) {
                                getSelectedEpisode(mediaId, season.id).run {
                                    if (this !in episodesResource.data.indices) null else this
                                }
                            } else null

                            _uiState.updateTvShowUiState {
                                it.copy(
                                    selectedSeasonEpisodes = episodesResource.data,
                                    selectedEpisodeIndex = selectedEpisode,
                                    tvShowEpisodesUiState = episodesResource.toUiState()
                                )
                            }
                        }
                }
        }
        // update poster data according to selected episode
        viewModelScope.launch {
            combine(
                selectedSeason.filterNotNull(),
                selectedEpisode.filterNotNull()
            ) { season, episode ->
                TvShowPosterData(
                    episodeNumber = "S${season.seasonNumber.withLeadingZeros(2)}E${episode.episodeNumber.withLeadingZeros(2)}",
                    episodeName = episode.title,
                    duration = episode.duration,
                    imageUrl = episode.imageUrl ?: season.imageUrl
                )
            }.collect { posterData ->
                _uiState.updateTvShowUiState { it.copy(posterData = posterData) }
            }
        }
    }

    fun onMediaDetailAction(action: MediaDetailAction) {
        when(action) {
            MediaDetailAction.PlayDefault -> {} // TODO
            is MediaDetailAction.PlayStream -> {
                _uiState.update { it.copy(selectedStreamIdent = action.ident) }
            }
            MediaDetailAction.PlayerLaunched -> {
                _uiState.update { it.copy(selectedStreamIdent = null) }
            }
            is MediaDetailAction.SelectTvShowSeason -> {
                _uiState.updateTvShowUiState { it.copy(selectedSeasonIndex = action.seasonIndex) }
            }
            is MediaDetailAction.SelectTvShowEpisode -> {
                if(action.episodeIndex != _uiState.value.tvShowUiState?.selectedEpisodeIndex) {
                    _uiState.update { it.copy(streams = null) }
                    _uiState.updateTvShowUiState { it.copy(selectedEpisodeIndex = action.episodeIndex) }
                }
            }
        }
    }
}

sealed class MediaDetailAction {
    object PlayDefault: MediaDetailAction()
    data class PlayStream(val ident: String): MediaDetailAction()
    object PlayerLaunched: MediaDetailAction()
    data class SelectTvShowSeason(val seasonIndex: Int): MediaDetailAction()
    data class SelectTvShowEpisode(val episodeIndex: Int): MediaDetailAction()
}

private val MediaDetailScreenUiState.tvShowUiState: TvShowMediaDetailUiState?
    get() = (mediaDetailUiState as? TvShowMediaDetailUiState)

fun MutableStateFlow<MediaDetailScreenUiState>.updateTvShowUiState(updateTvShowUiState: (TvShowMediaDetailUiState) -> TvShowMediaDetailUiState) {
    value.tvShowUiState?.let {
        update { mediaDetailUiState ->
            mediaDetailUiState.copy(mediaDetailUiState = updateTvShowUiState(it))
        }
    }
}