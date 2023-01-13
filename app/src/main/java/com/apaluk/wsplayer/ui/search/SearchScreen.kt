@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.apaluk.wsplayer.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.ui.common.composable.BackButton
import com.apaluk.wsplayer.ui.common.composable.DefaultEmptyState
import com.apaluk.wsplayer.ui.common.composable.UiStateAnimator
import com.apaluk.wsplayer.ui.common.util.stringResourceSafe
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()
    SearchScreenContent(
        modifier = modifier,
        uiState = uiState.value,
        onSearchTextChanged = viewModel::onSearchTextChanged,
        onSearch = viewModel::triggerSearch,
        onClearSearch = viewModel::clearSearch,
        onBack = { navController.navigateUp() }
    )
}

@Composable
private fun SearchScreenContent(
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    onSearchTextChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onClearSearch: () -> Unit,
    onBack: () -> Unit
) {
    val toolbarHeight = 82.dp
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = modifier
                    .height(toolbarHeight),
                navigationIcon = {
                    BackButton(
                        modifier = modifier.height(toolbarHeight),
                        onBack = { onBack() }
                    )
                },
                title = {
                    SearchBar(
                        modifier = modifier,
                        uiState = uiState,
                        onSearchTextChanged = onSearchTextChanged,
                        onSearch = onSearch,
                        onClearSearch = onClearSearch
                    )
                }
            )
        },
        content = { paddingValues ->
            UiStateAnimator(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                uiState = uiState.searchState,
                empty = { DefaultEmptyState(text = stringResourceSafe(id = R.string.wsp_search_empty_results))}
            ) {
                SearchResults(
                    modifier = modifier.padding(paddingValues),
                    results = uiState.searchResults,
                    onResultClicked = {},
                    scrollToTop = uiState.scrollListToTop
                )
            }
        }
    )
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    onSearchTextChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onClearSearch: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextField(
            modifier = modifier
                .weight(1f)
                .padding(top = 8.dp)
                .padding(horizontal = 8.dp)
                .fillMaxHeight()
                .focusRequester(focusRequester),
            value = uiState.searchText,
            textStyle = MaterialTheme.typography.titleLarge,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            onValueChange = { onSearchTextChanged(it) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search_24),
                    contentDescription = null
                )
            },
            trailingIcon = {
                if(uiState.searchText.isNotEmpty()) {
                    Icon(
                        modifier = modifier
                            .clickable {
                                keyboardController?.show()
                                onClearSearch()
                            },
                        painter = painterResource(id = R.drawable.ic_clear_24),
                        contentDescription = null
                    )
                }
            },
            placeholder = {
                Text(
                    text = stringResourceSafe(id = R.string.wsp_search_hint),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            maxLines = 1
        )
        Button(
            onClick = {
                keyboardController?.hide()
                onSearch()
            }
        ) {
            Text(text = stringResourceSafe(id = R.string.wsp_search_button))
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    WsPlayerTheme {
        SearchScreenContent(
            uiState = SearchUiState(),
            onSearchTextChanged = {},
            onSearch = {},
            onClearSearch = {},
            onBack = {}
        )
    }
}

