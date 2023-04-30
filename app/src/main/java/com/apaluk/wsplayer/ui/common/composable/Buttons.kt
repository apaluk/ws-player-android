@file:OptIn(ExperimentalUnitApi::class)

package com.apaluk.wsplayer.ui.common.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun WspButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    enabled: Boolean = true,
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        enabled = enabled
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = text,
            style = textStyle,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun WspOutlinedButton(
    text: String,
    onClick: () -> Unit,
    style: WspOutlinedButtonStyle
) {
    val outlineColor = when(style) {
        WspOutlinedButtonStyle.Light -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
        WspOutlinedButtonStyle.Highlighted -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
    }
    OutlinedButton(
        shape = MaterialTheme.shapes.extraSmall,
        border = BorderStroke(
            width = 2.dp,
            color = outlineColor,
        ),
        onClick = onClick
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
            letterSpacing = TextUnit(1.4f, TextUnitType.Sp),
            fontWeight = FontWeight.SemiBold,
        )
    }
}
    
enum class WspOutlinedButtonStyle {
    Light, Highlighted
}