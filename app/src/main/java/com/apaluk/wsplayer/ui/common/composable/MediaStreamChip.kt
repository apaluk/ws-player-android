package com.apaluk.wsplayer.ui.common.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apaluk.wsplayer.core.util.formatFileSize
import com.apaluk.wsplayer.core.util.formatTransferSpeed
import com.apaluk.wsplayer.domain.model.media.*
import com.apaluk.wsplayer.ui.common.util.color
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme

@Composable
fun MediaStreamChip(
    mediaStream: MediaStream,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClick(mediaStream.ident) }
            .padding(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(65.dp)
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = mediaStream.video.toString(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = mediaStream.video.color()
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .weight(1f)
        ) {
            Text(
                text = mediaStream.size.formatFileSize(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = mediaStream.speed.formatTransferSpeed(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .width(80.dp)
        ) {
            LanguageInfo(
                icon = com.apaluk.wsplayer.R.drawable.ic_chat_bubble_24,
                langs = mediaStream.audios,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            LanguageInfo(
                icon = com.apaluk.wsplayer.R.drawable.ic_subtitles_24,
                langs = mediaStream.subtitles.map { it.lang }.distinct(),
            )
        }
    }
}

@Composable
fun LanguageInfo(
    @DrawableRes icon: Int,
    langs: List<String>,
    modifier: Modifier = Modifier
) {
    if(langs.isNotEmpty()) {
        val text = langs.joinToString(
            separator = ",",
            limit = 2
        )

        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun MediaStreamChipPreview() {
    WsPlayerTheme {
        MediaStreamChip(
            mediaStream = DUMMY_MEDIA_STREAMS[1]
        )
    }
}

