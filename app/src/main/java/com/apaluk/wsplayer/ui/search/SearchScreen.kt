@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.apaluk.wsplayer.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.core.navigation.WsPlayerNavActions
import com.apaluk.wsplayer.ui.common.composable.BackButton
import com.apaluk.wsplayer.ui.common.composable.DefaultEmptyState
import com.apaluk.wsplayer.ui.common.composable.UiStateAnimator
import com.apaluk.wsplayer.ui.common.composable.WspButton
import com.apaluk.wsplayer.ui.common.util.stringResourceSafe
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navActions: WsPlayerNavActions
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()
    SearchScreenContent(
        modifier = modifier,
        uiState = uiState.value,
        onSearchScreenAction = viewModel::onSearchScreenAction,
        onBack = { navActions.navigateUp() }
    )
    LaunchedEffect(uiState.value.selectedMediaId) {
        uiState.value.selectedMediaId?.let {
            navActions.navigateToMediaId(it)
        }
        viewModel.onSearchScreenAction(SearchScreenAction.MediaSelected(null))
    }
}

@Composable
private fun SearchScreenContent(
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    onSearchScreenAction: (SearchScreenAction) -> Unit,
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
                        onSearchScreenAction = onSearchScreenAction
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
                    onResultClicked = { onSearchScreenAction(SearchScreenAction.MediaSelected(it)) },
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
    onSearchScreenAction: (SearchScreenAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var keyboardShown = remember { false }
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
            onValueChange = { onSearchScreenAction(SearchScreenAction.SearchTextChanged(it)) },
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
                                onSearchScreenAction(SearchScreenAction.ClearSearch)
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
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onSearchScreenAction(SearchScreenAction.TriggerSearch)
                }
            ),
            maxLines = 1
        )
        WspButton(
            text = stringResourceSafe(id = R.string.wsp_search_button),
            onClick = {
                keyboardController?.hide()
                onSearchScreenAction(SearchScreenAction.TriggerSearch)
            },
            enabled = uiState.searchText.isNotBlank(),
            textStyle = MaterialTheme.typography.bodyMedium
        )
    }
    LaunchedEffect(uiState.showKeyboard) {
        if(uiState.showKeyboard) {
            focusRequester.requestFocus()
            onSearchScreenAction(SearchScreenAction.KeyboardShown)
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    WsPlayerTheme {
        SearchScreenContent(
            uiState = SearchUiState(),
            onSearchScreenAction = {},
            onBack = {}
        )
    }
}

