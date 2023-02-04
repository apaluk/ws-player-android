package com.apaluk.wsplayer.ui.common.util

import androidx.annotation.StringRes

sealed class UiState {
    object Idle: UiState()
    object Loading: UiState()
    object Content: UiState()
    object Empty: UiState()
    data class Error(
        val text: String? = null,
        @StringRes val textId: Int? = null
    ): UiState()
}
