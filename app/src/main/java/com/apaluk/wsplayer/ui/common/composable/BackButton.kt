package com.apaluk.wsplayer.ui.common.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = { onBack() }
    ) {
        Icon(
            modifier = modifier,
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back",
        )
    }
}