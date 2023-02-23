package com.apaluk.wsplayer.ui.media_detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.core.util.Constants
import com.apaluk.wsplayer.domain.model.media.MediaDetailTvShow
import com.apaluk.wsplayer.domain.model.media.TvShowEpisode
import com.apaluk.wsplayer.domain.model.media.TvShowSeason
import com.apaluk.wsplayer.ui.common.composable.MediaTitle
import com.apaluk.wsplayer.ui.common.composable.UiStateAnimator
import com.apaluk.wsplayer.ui.common.util.UiState
import com.apaluk.wsplayer.ui.common.util.stringResourceSafe
import com.apaluk.wsplayer.ui.media_detail.util.generalInfoText
import com.apaluk.wsplayer.ui.media_detail.util.requireName
import com.apaluk.wsplayer.ui.media_detail.util.selectedSeasonName

@Composable
fun MediaDetailTvShowContent(
    mediaDetailTvShow: MediaDetailTvShow,
    episodesUiState: UiState,
    onMediaDetailAction: (MediaDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showSeasonSelectorDialog = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        MediaDetailPoster(
            imageUrl = mediaDetailTvShow.imageUrl,
            onPlay = { onMediaDetailAction(MediaDetailAction.PlayDefault) }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MediaTitle(
                title = mediaDetailTvShow.title,
                originalTitle = mediaDetailTvShow.originalTitle
            )
            Spacer(modifier = Modifier.weight(1f))
            mediaDetailTvShow.selectedSeasonName()?.let { seasonName ->
                DropDownSelector(
                    text = seasonName,
                    onClick = { showSeasonSelectorDialog.value = true }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = mediaDetailTvShow.generalInfoText(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        MediaDetailTvShowEpisodesList(
            tvShow = mediaDetailTvShow,
            uiState = episodesUiState,
            onMediaDetailAction = onMediaDetailAction
        )
        Spacer(modifier = Modifier.height(64.dp))
    }
    if (showSeasonSelectorDialog.value) {
        mediaDetailTvShow.seasons?.let { seasons ->
            SelectSeasonDialog(
                seasons = seasons,
                onSeasonIndexSelected = {
                    onMediaDetailAction(MediaDetailAction.SelectTvShowSeason(it))
                    showSeasonSelectorDialog.value = false
                },
                onDismiss = { showSeasonSelectorDialog.value = false }
            )
        }
    }
}

@Composable
private fun SelectSeasonDialog(
    seasons: List<TvShowSeason>,
    onSeasonIndexSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Card(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                itemsIndexed(seasons) { index, season ->
                    Text(
                        text = season.requireName(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSeasonIndexSelected(index) }
                            .padding(horizontal = 32.dp, vertical = 16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
fun MediaDetailTvShowEpisodesList(
    tvShow: MediaDetailTvShow,
    uiState: UiState,
    onMediaDetailAction: (MediaDetailAction) -> Unit
) {
    UiStateAnimator(
        uiState = uiState,
        modifier = Modifier.heightIn(min = 140.dp)
    ) {
        val episodes = tvShow.selectedSeasonEpisodes ?: return@UiStateAnimator
        Column {
            Divider(
                color = MaterialTheme.colorScheme.surfaceVariant,
                thickness = 1.dp
            )
            episodes.forEachIndexed { index, episode ->
                MediaDetailTvShowEpisode(
                    episode = episode,
                    onSelected = { onMediaDetailAction(MediaDetailAction.SelectTvShowEpisode(index)) },
                    isSelected = index == tvShow.selectedEpisodeIndex
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
fun MediaDetailTvShowEpisode(
    episode: TvShowEpisode,
    onSelected: () -> Unit,
    isSelected: Boolean = false
) {
    val background = if(isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.background
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelected() }
            .background(background)
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(80.dp)
                .background(color = MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            if (!episode.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    modifier = Modifier
                        .background(background)
                        .padding(start = 4.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(episode.imageUrl)
                        .crossfade(durationMillis = Constants.SHORT_ANIM_DURATION)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(vertical = 12.dp),
                text = episode.title ?: stringResourceSafe(
                    id = R.string.wsp_tv_show_episode_number,
                    episode.episodeNumber
                ),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}