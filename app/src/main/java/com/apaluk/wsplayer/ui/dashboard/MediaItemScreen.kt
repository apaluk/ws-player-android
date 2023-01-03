package com.apaluk.wsplayer.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.domain.model.media.MediaStream
import com.apaluk.wsplayer.ui.common.composable.FullScreenLoader
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme

@Composable
fun MediaItemScreen(
    modifier: Modifier = Modifier,
    viewModel: MediaItemViewModel = hiltViewModel(),
    onMediaIdSelected: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    if(uiState.isLoading) {
        FullScreenLoader()
    } else {
        uiState.streams?.let {
            LazyColumn {
                items(it) { stream ->
                    MediaStreamItem(
                        stream = stream,
                        modifier = modifier,
                        onStreamSelected = viewModel::onStreamSelected
                    )
                }
            }
        }
    }
    LaunchedEffect(uiState.selectedVideoUrl) {
        uiState.selectedVideoUrl?.let {
            onMediaIdSelected(it)
            viewModel.clearSelectedVideoUrl()
        }
    }
}

@Composable
fun MediaStreamItem(
    stream: MediaStream,
    modifier: Modifier = Modifier,
    onStreamSelected: ((String) -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onStreamSelected?.invoke(stream.ident)
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_play_24),
            contentDescription = "Play"
        )
        Spacer(modifier = modifier.width(8.dp))
        Text(
            text = stream.toString()
        )
    }
}

@Preview
@Composable
fun MediaStreamPreview() {
    WsPlayerTheme {
        MediaStream("1", "ident", listOf("en", "sk"), listOf("fullHd"))
    }
}
