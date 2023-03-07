package com.apaluk.wsplayer.ui.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apaluk.wsplayer.core.login.LoginManager
import com.apaluk.wsplayer.core.navigation.WsPlayerNavArgs
import com.apaluk.wsplayer.core.util.SingleEvent
import com.apaluk.wsplayer.data.webshare.WebShareRepository
import com.apaluk.wsplayer.domain.use_case.webshare.GetFileLinkUseCase
import com.apaluk.wsplayer.ui.common.util.UiState
import com.apaluk.wsplayer.ui.common.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    getFileLinkUseCase: GetFileLinkUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val webshareIdent: String = requireNotNull(savedStateHandle[WsPlayerNavArgs.WEBSHARE_IDENT_ARG])

    private val _uiState = MutableStateFlow(PlayerScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getFileLinkUseCase(webshareIdent).map { resource ->
                _uiState.update {
                    it.copy(
                        uiState = resource.toUiState(),
                        videoUrl = resource.data
                    )
                }
            }.collect()
        }
    }

    fun onPlayerScreenAction(action: PlayerScreenAction) {
        when(action) {
            PlayerScreenAction.VideoEnded -> onVideoEnded()
            is PlayerScreenAction.VideoProgressChanged -> TODO()
        }
    }

    private fun onVideoEnded() {
        viewModelScope.launch {
            _uiState.value.navigateUpEvent.emit(Unit)
        }
    }
}

data class PlayerScreenState(
    val uiState: UiState = UiState.Loading,
    val videoUrl: String? = null,
    val navigateUpEvent: SingleEvent<Unit> = SingleEvent()
)

sealed class PlayerScreenAction {
    object VideoEnded: PlayerScreenAction()
    data class VideoProgressChanged(val playbackTime: Int): PlayerScreenAction()
}