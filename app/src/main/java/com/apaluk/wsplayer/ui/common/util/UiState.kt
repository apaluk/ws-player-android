package com.apaluk.wsplayer.ui.common.util

sealed class UiState {
    object Idle: UiState()
    object Loading: UiState()
    object Content: UiState()
    object Empty: UiState()
    data class Error(val text: String?): UiState()
}
