package com.apaluk.wsplayer.ui.common.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.apaluk.wsplayer.core.util.hashing.ComposableFun
import com.apaluk.wsplayer.ui.common.util.UiState

@Composable
fun UiStateAnimator(
    uiState: UiState,
    modifier: Modifier = Modifier,
    content: ComposableFun
) {
    when(uiState) {
        UiState.Loading -> FullScreenLoader(modifier)
        UiState.Content -> content()
    }
}