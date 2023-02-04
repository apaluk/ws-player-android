package com.apaluk.wsplayer.ui.media_detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.core.navigation.WsPlayerNavActions
import com.apaluk.wsplayer.core.util.Constants
import com.apaluk.wsplayer.core.util.formatDuration
import com.apaluk.wsplayer.domain.model.media.DUMMY_MEDIA_STREAMS
import com.apaluk.wsplayer.domain.model.media.MediaDetail
import com.apaluk.wsplayer.ui.common.composable.BackButton
import com.apaluk.wsplayer.ui.common.composable.MovieTitle
import com.apaluk.wsplayer.ui.common.composable.TextOnImage
import com.apaluk.wsplayer.ui.common.composable.UiStateAnimator
import com.apaluk.wsplayer.ui.common.util.UiState
import com.apaluk.wsplayer.ui.common.util.stringResourceSafe
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
            BackButton(
                onBack = { navActions.navigateUp() }
            )
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
    onMediaDetailAction: (MediaDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top
    ) {
        uiState.mediaDetail?.let { mediaDetail ->
            MediaDetail(
                mediaDetail = mediaDetail,
                onMediaDetailAction = onMediaDetailAction,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 76.dp, end = 24.dp)
                    .align(Alignment.Top)
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

@Composable
fun MediaDetail(
    mediaDetail: MediaDetail,
    onMediaDetailAction: (MediaDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        MediaDetailPoster(mediaDetail = mediaDetail) {
            onMediaDetailAction(MediaDetailAction.PlayDefault)
        }
        MovieTitle(
            modifier = Modifier.padding(top = 16.dp),
            title = mediaDetail.title,
            originalTitle = mediaDetail.originalTitle
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = mediaDetail.generalInfoText(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        CrewMembers(
            role = stringResourceSafe(id = R.string.wsp_media_director),
            members = mediaDetail.directors,
            modifier = Modifier.padding(top = 16.dp)
        )
        CrewMembers(
            modifier = Modifier.padding(top = 8.dp),
            role = stringResourceSafe(id = R.string.wsp_media_writer),
            members = mediaDetail.writer
        )
        CrewMembers(
            modifier = Modifier.padding(top = 8.dp),
            role = stringResourceSafe(id = R.string.wsp_media_cast),
            members = mediaDetail.cast
        )
        mediaDetail.plot?.let { plot ->
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                text = plot,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun MediaDetailPoster(
    mediaDetail: MediaDetail,
    onPlay: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(if (mediaDetail.imageUrl == null) 3f else 2f)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        mediaDetail.imageUrl?.let { url ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .crossfade(durationMillis = Constants.SHORT_ANIM_DURATION)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_play_filled_circle_64),
            contentDescription = stringResourceSafe(id = R.string.wsp_media_play),
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f),
                    shape = MaterialTheme.shapes.extraLarge
                )
                .clickable { onPlay() }
                .align(Alignment.Center)
                .alpha(0.7f),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
        )
        TextOnImage(
            text = mediaDetail.duration.formatDuration(),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        )
    }

}

@Composable
fun CrewMembers(
    role: String,
    members: List<String>,
    modifier: Modifier = Modifier
) {
    Row {
        Text(
            modifier = modifier
                .alignByBaseline()
                .padding(end = 8.dp),
            text = "$role:",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = modifier.alignByBaseline(),
            text = members
                .take(12)
                .joinToString(", "),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private fun MediaDetail.generalInfoText(): String {
    val separator = "  ${Constants.CHAR_BULLET}  "
    with(StringBuilder()) {
        year?.let {
            append(it).append(separator)
        }
        if(genre.isNotEmpty()) {
            append(genre.joinToString(separator = " / "))
        }
        return toString()
    }
}

@Preview(device = "spec:width=1600dp,height=800dp,orientation=landscape")
@Composable
fun MediaDetailContentPreview() {
    WsPlayerTheme {
        MediaDetailScreenContent(
            uiState = MediaDetailUiState(
                uiState = UiState.Content,
                mediaDetail = MediaDetail(
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
                    duration = 7444
                ),
                streams = DUMMY_MEDIA_STREAMS
            ),
            onMediaDetailAction = {}
        )
    }
}
