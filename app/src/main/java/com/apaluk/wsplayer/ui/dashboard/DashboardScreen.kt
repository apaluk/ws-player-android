package com.apaluk.wsplayer.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apaluk.wsplayer.domain.model.media.Media
import com.apaluk.wsplayer.ui.common.composable.FullScreenLoader

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onMediaIdSelected: (String) -> Unit
) {
    val viewModel: DashboardViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()
    DashboardContent(
        modifier = modifier,
        uiState = uiState.value,
        onMediaSelected = viewModel::onMediaSelected
    )
    LaunchedEffect(uiState.value.selectedMediaId) {
        val selectedVideoUrl = uiState.value.selectedMediaId ?: return@LaunchedEffect
        onMediaIdSelected(selectedVideoUrl)
        viewModel.clearSelectedMedia()
    }
}

@Composable
private fun DashboardContent(
    modifier: Modifier,
    uiState: DashboardUiState,
    onMediaSelected: (String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (uiState.isLoading) {
            FullScreenLoader()
        } else {
            LazyColumn {
                items(
                    items = uiState.items,
                    key = { it.id }
                ) { media ->
                    DashboardMediaItem(
                        media = media,
                        onClicked = { onMediaSelected(media.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardMediaItem(
    modifier: Modifier = Modifier,
    media: Media,
    onClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        Text(
            text = media.name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = modifier.clickable { onClicked() }
        )
    }
}