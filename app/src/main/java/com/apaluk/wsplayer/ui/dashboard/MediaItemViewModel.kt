package com.apaluk.wsplayer.ui.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apaluk.wsplayer.core.navigation.WsPlayerNavArgs
import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.data.stream_cinema.StreamCinemaRepository
import com.apaluk.wsplayer.data.webshare.WebShareRepository
import com.apaluk.wsplayer.domain.model.media.MediaStream
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MediaItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    streamCinemaRepository: StreamCinemaRepository,
    private val webShareRepository: WebShareRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(MediaItemUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val mediaId: String? = savedStateHandle[WsPlayerNavArgs.MEDIA_ID_ARG]
            mediaId?.let {
                val streamsResource = streamCinemaRepository.getMediaStreams(mediaId).last()
                _uiState.update { it.copy(isLoading = false) }
                if(streamsResource is Resource.Success && streamsResource.data != null) {
                    _uiState.update { it.copy(streams = streamsResource.data) }
                }
            }
        }
    }

    fun onStreamSelected(streamIdent: String) {
        viewModelScope.launch {
            val link = webShareRepository.getFileLink(streamIdent).last()
            Timber.d("xxx file link=${link.data}")
            _uiState.update { it.copy(selectedVideoUrl = link.data) }
        }
    }

    fun clearSelectedVideoUrl() {
        _uiState.update { it.copy(selectedVideoUrl = null) }
    }
}

data class MediaItemUiState(
    val isLoading: Boolean = true,
    val streams: List<MediaStream>? = null,
    val selectedVideoUrl: String? = null
)