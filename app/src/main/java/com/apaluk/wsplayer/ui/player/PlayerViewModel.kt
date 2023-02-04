package com.apaluk.wsplayer.ui.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apaluk.wsplayer.core.login.LoginManager
import com.apaluk.wsplayer.core.navigation.WsPlayerNavArgs
import com.apaluk.wsplayer.data.webshare.WebShareRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    webShareRepository: WebShareRepository,
    loginManager: LoginManager,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val webshareIdent: String = requireNotNull(savedStateHandle[WsPlayerNavArgs.WEBSHARE_IDENT_ARG])

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = webShareRepository.getFileLink(loginManager.getWebShareToken(), webshareIdent).last()
            result.data?.let { url ->
                _uiState.update { it.copy(videoUrl = url) }
            }
        }
    }
}

data class PlayerUiState(
    val videoUrl: String? = null
)