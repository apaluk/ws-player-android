package com.apaluk.wsplayer.ui.media_detail.movie

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.ui.common.composable.MediaTitle
import com.apaluk.wsplayer.ui.common.util.stringResourceSafe
import com.apaluk.wsplayer.ui.media_detail.common.CrewMembers
import com.apaluk.wsplayer.ui.media_detail.MediaDetailAction
import com.apaluk.wsplayer.ui.media_detail.MovieMediaDetailUiState
import com.apaluk.wsplayer.ui.media_detail.common.MediaDetailPoster
import com.apaluk.wsplayer.ui.media_detail.util.generalInfoText

@Composable
fun MovieMediaDetailContent(
    movieUiState: MovieMediaDetailUiState,
    onMediaDetailAction: (MediaDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mediaDetailMovie = movieUiState.movie
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        MediaDetailPoster(
            imageUrl = mediaDetailMovie.imageUrl,
            duration = mediaDetailMovie.duration,
            onPlay = { onMediaDetailAction(MediaDetailAction.PlayDefault) }
        )
        Spacer(modifier = Modifier.height(24.dp))
        MediaTitle(
            title = mediaDetailMovie.title,
            originalTitle = mediaDetailMovie.originalTitle
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = mediaDetailMovie.generalInfoText(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        CrewMembers(
            role = stringResourceSafe(id = R.string.wsp_media_director),
            members = mediaDetailMovie.directors,
            modifier = Modifier.padding(top = 16.dp)
        )
        CrewMembers(
            modifier = Modifier.padding(top = 8.dp),
            role = stringResourceSafe(id = R.string.wsp_media_writer),
            members = mediaDetailMovie.writer
        )
        CrewMembers(
            modifier = Modifier.padding(top = 8.dp),
            role = stringResourceSafe(id = R.string.wsp_media_cast),
            members = mediaDetailMovie.cast
        )
        mediaDetailMovie.plot?.let { plot ->
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