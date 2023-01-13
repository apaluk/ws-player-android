@file:OptIn(ExperimentalMaterial3Api::class)

package com.apaluk.wsplayer.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.ui.common.composable.TopAppBarAction
import com.apaluk.wsplayer.ui.common.util.stringResourceSafe
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onSearch: () -> Unit
) {
    val viewModel: DashboardViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    TopAppBarAction(
                        icon = R.drawable.ic_search_24,
                        onClick = { onSearch() }
                    )
                }
            )
        },
        content = { padding ->
            DashboardContent(
                modifier = modifier.padding(padding),
                uiState = uiState.value
            )
        }
    )
}

@Composable
private fun DashboardContent(
    modifier: Modifier = Modifier,
    uiState: DashboardUiState
) {
}

@Preview
@Composable
fun DashboardPreview() {
    WsPlayerTheme {
        DashboardContent(
            uiState = DashboardUiState(),
        )
    }
}

