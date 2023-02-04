@file:OptIn(ExperimentalMaterial3Api::class)

package com.apaluk.wsplayer.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.core.navigation.WsPlayerNavActions
import com.apaluk.wsplayer.ui.common.composable.TopAppBarAction
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    navActions: WsPlayerNavActions
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
                        onClick = { navActions.navigateToSearch() }
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

