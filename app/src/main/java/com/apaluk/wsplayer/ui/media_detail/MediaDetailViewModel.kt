package com.apaluk.wsplayer.ui.media_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.core.navigation.WsPlayerNavArgs
import com.apaluk.wsplayer.domain.model.media.MediaDetail
import com.apaluk.wsplayer.domain.model.media.MediaDetailTvShow
import com.apaluk.wsplayer.domain.model.media.MediaStream
import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import com.apaluk.wsplayer.domain.use_case.media.GetMediaDetailUseCase
import com.apaluk.wsplayer.domain.use_case.media.GetSelectedEpisodeUseCase
import com.apaluk.wsplayer.domain.use_case.media.GetSelectedSeasonUseCase
import com.apaluk.wsplayer.ui.common.util.UiState
import com.apaluk.wsplayer.ui.common.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailViewModel @Inject constructor(
    private val streamCinemaRepository: StreamCinemaRepository,
    getMediaDetail: GetMediaDetailUseCase,
    getSelectedSeason: GetSelectedSeasonUseCase,
    getSelectedEpisode: GetSelectedEpisodeUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val mediaId: String = requireNotNull(savedStateHandle[WsPlayerNavArgs.MEDIA_ID_ARG])

    private val _uiState = MutableStateFlow(MediaDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val selectedEpisode = combine(
        _uiState.mapNotNull { it.mediaDetailTvShow?.selectedEpisodeIndex }.distinctUntilChanged(),
        _uiState.mapNotNull { it.mediaDetailTvShow?.selectedSeasonEpisodes }.distinctUntilChanged()
    ) { index, episodes ->
        episodes.getOrNull(index)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val mediaIdForStreams: StateFlow<String> = selectedEpisode
        .filterNotNull()
        .map { it.id }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), mediaId)

    init {
        // get media detail
        viewModelScope.launch {
            getMediaDetail(mediaId).collect { mediaDetailResource ->
                _uiState.update {
                    it.copy(
                        uiState = mediaDetailResource.toUiState(),
                        mediaDetail = mediaDetailResource.data,
                    )
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
        // set default selected season when seasons are fetched
        viewModelScope.launch {
            _uiState
                .mapNotNull { it.mediaDetail }
                .filterIsInstance<MediaDetailTvShow>()
                .mapNotNull { it.seasons }
                .distinctUntilChanged()
                .collect {
                    val selectedSeasonIndex = getSelectedSeason(mediaId, it)
                    _uiState.updateTvShow { tvShow ->
                        tvShow.copy(selectedSeasonIndex = selectedSeasonIndex)
                    }
                }
        }
        // get episodes on season select
        viewModelScope.launch {
            combine(
                _uiState.mapNotNull { it.mediaDetailTvShow?.seasons }.distinctUntilChanged(),
                _uiState.mapNotNull { it.mediaDetailTvShow?.selectedSeasonIndex }.distinctUntilChanged()
            ) { seasons, seasonIndex ->
                if (seasonIndex in seasons.indices) {
                    streamCinemaRepository
                        .getTvShowSeasonEpisodes(seasons[seasonIndex].id)
                        .collect { episodesResource ->
                            val selectedEpisode = if(episodesResource.data != null) {
                                getSelectedEpisode(mediaId, seasonIndex).run {
                                    if (this !in episodesResource.data.indices) null else this
                                }
                            } else null

                            _uiState.update { it.copy(tvShowEpisodesUiState = episodesResource.toUiState()) }
                            _uiState.updateTvShow { it.copy(
                                selectedSeasonEpisodes = episodesResource.data,
                                selectedEpisodeIndex = selectedEpisode
                            ) }
                        }
                } else {
                    _uiState.update { it.copy(tvShowEpisodesUiState = UiState.Error(textId = R.string.wsp_default_error_state)) }
                    _uiState.updateTvShow { it.copy(selectedSeasonEpisodes = null) }
                }
            }.collect()
        }
        // update poster image according to selected episode
        viewModelScope.launch {
            selectedEpisode
                .mapNotNull { it?.imageUrl }
                .collect { imageUrl ->
                    _uiState.updateTvShow { it.copy(imageUrl = imageUrl) }
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
                _uiState.updateTvShow { it.copy(selectedSeasonIndex = action.seasonIndex) }
            }
            is MediaDetailAction.SelectTvShowEpisode -> {
                if(action.episodeIndex != _uiState.value.mediaDetailTvShow?.selectedEpisodeIndex) {
                    _uiState.update { it.copy(streams = null) }
                    _uiState.updateTvShow { it.copy(selectedEpisodeIndex = action.episodeIndex) }
                }
            }
        }
    }
}

data class MediaDetailUiState(
    val uiState: UiState = UiState.Loading,
    val mediaDetail: MediaDetail? = null,
    val streams: List<MediaStream>? = null,
    val tvShowEpisodesUiState: UiState = UiState.Idle,
    val selectedStreamIdent: String? = null
)

sealed class MediaDetailAction {
    object PlayDefault: MediaDetailAction()
    data class PlayStream(val ident: String): MediaDetailAction()
    object PlayerLaunched: MediaDetailAction()
    data class SelectTvShowSeason(val seasonIndex: Int): MediaDetailAction()
    data class SelectTvShowEpisode(val episodeIndex: Int): MediaDetailAction()
}

private val MediaDetailUiState.mediaDetailTvShow: MediaDetailTvShow?
    get() = (mediaDetail as? MediaDetailTvShow)

fun MutableStateFlow<MediaDetailUiState>.updateTvShow(updateTvShow: (MediaDetailTvShow) -> MediaDetailTvShow) {
    value.mediaDetailTvShow?.let {
        update { mediaDetailUiState ->
            mediaDetailUiState.copy(mediaDetail = updateTvShow(it))
        }
    }
}