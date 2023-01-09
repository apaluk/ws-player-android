package com.apaluk.wsplayer.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apaluk.wsplayer.domain.model.media.Media
import com.apaluk.wsplayer.R

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
        onSearchTextChanged = viewModel::onSearchTextChanged,
        onSearch = viewModel::triggerSearch,
        onMediaSelected = viewModel::onMediaSelected
    )
    LaunchedEffect(uiState.value.selectedMediaId) {
        val selectedVideoUrl = uiState.value.selectedMediaId ?: return@LaunchedEffect
        onMediaIdSelected(selectedVideoUrl)
        viewModel.clearSelectedMedia()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardContent(
    modifier: Modifier,
    uiState: DashboardUiState,
    onSearchTextChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onMediaSelected: (String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(modifier = modifier.fillMaxWidth()) {
            Image(
                modifier = modifier.padding(16.dp),
                painter = painterResource(id = R.drawable.ic_search_24),
                contentDescription = null
            )
            TextField(
                modifier = modifier.weight(1f),
                value = uiState.searchText,
                onValueChange = { onSearchTextChanged(it) }
            )
            Button(
                modifier = modifier.padding(16.dp),
                onClick = { onSearch() }
            ) {
                Text(text = "Search")
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

