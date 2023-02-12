package com.apaluk.wsplayer.ui.media_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apaluk.wsplayer.core.navigation.WsPlayerNavArgs
import com.apaluk.wsplayer.domain.model.media.MediaDetail
import com.apaluk.wsplayer.domain.model.media.MediaStream
import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import com.apaluk.wsplayer.ui.common.util.UiState
import com.apaluk.wsplayer.ui.common.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailViewModel @Inject constructor(
    streamCinemaRepository: StreamCinemaRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val mediaId: String = requireNotNull(savedStateHandle[WsPlayerNavArgs.MEDIA_ID_ARG])

    private val _uiState = MutableStateFlow(MediaDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val mediaDetailResource = streamCinemaRepository.getMediaDetails(mediaId).last()
            _uiState.update {
                it.copy(
                    uiState = mediaDetailResource.toUiState(),
                    mediaDetail = mediaDetailResource.data
                )
            }
        }
        viewModelScope.launch {
            val streamsResource = streamCinemaRepository.getMediaStreams(mediaId).last()
            _uiState.update {
                it.copy(streams = streamsResource.data?.sortedBy { stream -> stream.size })
            }
        }
    }

    fun onMediaDetailAction(action: MediaDetailAction) {
        when(action) {
            MediaDetailAction.PlayDefault -> {} // TODO
            is MediaDetailAction.PlayStream -> {
                _uiState.update {
                    it.copy(selectedStreamIdent = action.ident)
                }
            }
            MediaDetailAction.PlayerLaunched -> {
                _uiState.update {
                    it.copy(selectedStreamIdent = null)
                }
            }
        }

    }
}

data class MediaDetailUiState(
    val uiState: UiState = UiState.Loading,
    val mediaDetail: MediaDetail? = null,
    val streams: List<MediaStream>? = null,
    val selectedStreamIdent: String? = null
)

sealed class MediaDetailAction {
    object PlayDefault: MediaDetailAction()
    data class PlayStream(val ident: String): MediaDetailAction()
    object PlayerLaunched: MediaDetailAction()
}