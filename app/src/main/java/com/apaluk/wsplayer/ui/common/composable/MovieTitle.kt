package com.apaluk.wsplayer.ui.common.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun MovieTitle(
    title: String,
    originalTitle: String?,
    modifier: Modifier = Modifier
) {
    Row {
        Text(
            modifier = modifier.alignByBaseline(),
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
        if (originalTitle != null && originalTitle != title) {
            Text(
                modifier = modifier.padding(start = 6.dp).alignByBaseline(),
                text = "($originalTitle)",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}