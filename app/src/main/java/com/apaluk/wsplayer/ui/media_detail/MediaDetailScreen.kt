package com.apaluk.wsplayer.ui.media_detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apaluk.wsplayer.core.navigation.WsPlayerNavActions
import com.apaluk.wsplayer.core.util.exhaustive
import com.apaluk.wsplayer.domain.model.media.*
import com.apaluk.wsplayer.ui.common.composable.BackButton
import com.apaluk.wsplayer.ui.common.composable.UiStateAnimator
import com.apaluk.wsplayer.ui.common.util.UiState
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailScreen(
    modifier: Modifier = Modifier,
    navActions: WsPlayerNavActions,
    viewModel: MediaDetailViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    TopAppBar(
        navigationIcon = {
            BackButton(onBack = { navActions.navigateUp() })
        },
        title = {}
    )
    UiStateAnimator(uiState = uiState.value.uiState) {
        MediaDetailScreenContent(
            modifier = modifier,
            uiState = uiState.value,
            onMediaDetailAction = viewModel::onMediaDetailAction
        )
    }
    LaunchedEffect(uiState.value.selectedStreamIdent) {
        uiState.value.selectedStreamIdent?.let {
            navActions.navigateToPlayer(it)
            viewModel.onMediaDetailAction(MediaDetailAction.PlayerLaunched)
        }
    }
}

@Composable
fun MediaDetailScreenContent(
    uiState: MediaDetailUiState,
    modifier: Modifier = Modifier,
    onMediaDetailAction: (MediaDetailAction) -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top
    ) {
        uiState.mediaDetail?.let { mediaDetail ->
            MediaDetailContent(
                mediaDetail = mediaDetail,
                onMediaDetailAction = onMediaDetailAction,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 76.dp, end = 24.dp)
                    .align(Alignment.Top),
                episodesUiState = uiState.tvShowEpisodesUiState
            )
        }
        Box(
            modifier = modifier
                .width(340.dp)
                .padding(8.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.BottomCenter
        ) {
            uiState.streams?.let {
                if(it.isNotEmpty()) {
                    MediaDetailStreams(
                        streams = it,
                        modifier = modifier.fillMaxWidth(),
                        onStreamSelected = { stream ->
                            onMediaDetailAction(MediaDetailAction.PlayStream(stream))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MediaDetailContent(
    mediaDetail: MediaDetail,
    episodesUiState: UiState,
    modifier: Modifier = Modifier,
    onMediaDetailAction: (MediaDetailAction) -> Unit = {}
) {
    when(mediaDetail) {
        is MediaDetailMovie -> MediaDetailMovieContent(
            mediaDetailMovie = mediaDetail,
            onMediaDetailAction = onMediaDetailAction,
            modifier = modifier
        )
        is MediaDetailTvShow -> MediaDetailTvShowContent(
            mediaDetailTvShow = mediaDetail,
            episodesUiState = episodesUiState,
            onMediaDetailAction = onMediaDetailAction,
            modifier = modifier
        )
    }.exhaustive
}

@Preview(device = "spec:width=1600dp,height=800dp,orientation=landscape")
@Composable
fun MediaDetailContentPreview() {
    WsPlayerTheme {
        MediaDetailScreenContent(
            uiState = MediaDetailUiState(
                uiState = UiState.Content,
                mediaDetail = MediaDetailMovie(
                    id = "",
                    title = "Pulp fiction",
                    originalTitle = "Pulp fiction",
                    year = "1994",
                    directors = listOf("Quentin Tarantino"),
                    writer = listOf("Quentin Tarantino"),
                    cast = listOf("Bruce Willis", "John Travolta", "Samuel L. Jackson"),
                    genre = listOf("Thriller", "Comedy"),
                    plot = LoremIpsum(50).values.joinToString(" "),
                    imageUrl = null,
                    duration = 7444,
                ),
                streams = DUMMY_MEDIA_STREAMS
            ),
            onMediaDetailAction = {}
        )
    }
}
