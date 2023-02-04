package com.apaluk.wsplayer.ui.common.util

import androidx.compose.runtime.Composable
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.core.util.isNullOrEmptyList

fun <T> Resource<T>.toUiState(): UiState =
    when(this) {
        is Resource.Loading -> UiState.Loading
        is Resource.Error -> UiState.Error(message)
        is Resource.Success -> if(data.isNullOrEmptyList()) UiState.Empty else UiState.Content
    }

@Composable fun UiState.Error.text(): String =
    text ?:stringResourceSafe(id = textId ?: R.string.wsp_default_error_state)