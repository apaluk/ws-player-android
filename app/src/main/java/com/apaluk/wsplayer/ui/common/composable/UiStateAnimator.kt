package com.apaluk.wsplayer.ui.common.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.core.util.hashing.ComposableFun
import com.apaluk.wsplayer.ui.common.util.UiState
import com.apaluk.wsplayer.ui.common.util.stringResourceSafe

@Composable
fun UiStateAnimator(
    uiState: UiState,
    modifier: Modifier = Modifier,
    loading: ComposableFun = { FullScreenLoader(modifier) },
    empty: ComposableFun = { DefaultEmptyState(modifier) },
    error: @Composable (String) -> Unit = { text -> DefaultErrorState(text = text) },
    content: ComposableFun
) {
    when(uiState) {
        UiState.Idle -> {}
        UiState.Loading -> loading()
        UiState.Content -> content()
        UiState.Empty -> empty()
        is UiState.Error -> error(uiState.text ?: stringResourceSafe(id = R.string.wsp_default_error_state))
    }
}

@Composable
fun FullScreenLoader(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun DefaultEmptyState(
    modifier: Modifier = Modifier,
    text: String = stringResourceSafe(id = R.string.wsp_default_empty_state),
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DefaultErrorState(
    modifier: Modifier = Modifier,
    text: String = stringResourceSafe(id = R.string.wsp_default_error_state),
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
        }
    }

}