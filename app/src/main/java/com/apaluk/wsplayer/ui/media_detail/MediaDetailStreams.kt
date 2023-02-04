package com.apaluk.wsplayer.ui.media_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.domain.model.media.DUMMY_MEDIA_STREAMS
import com.apaluk.wsplayer.domain.model.media.MediaStream
import com.apaluk.wsplayer.ui.common.composable.MediaStreamChip
import com.apaluk.wsplayer.ui.common.util.stringResourceSafe
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme

@Composable
fun MediaDetailStreams(
    streams: List<MediaStream>,
    modifier: Modifier = Modifier,
    onStreamSelected: (String) -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(8.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResourceSafe(id = R.string.wsp_media_select_stream),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            LazyColumn {
                itemsIndexed(streams) { index, stream ->
                    MediaStreamChip(
                        mediaStream = stream,
                        modifier = modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .fillMaxWidth(),
                        onClick = { onStreamSelected(it) }
                    )
                    if(index == streams.lastIndex) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
            
        }
    }
}

@Preview
@Composable
fun MediaDetailStreamsPreview() {
    WsPlayerTheme {
        MediaDetailStreams(streams = DUMMY_MEDIA_STREAMS)
    }
}