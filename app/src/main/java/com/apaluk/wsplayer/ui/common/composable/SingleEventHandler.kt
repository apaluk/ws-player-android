package com.apaluk.wsplayer.ui.common.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.apaluk.wsplayer.core.util.SingleEvent

@Composable
fun <T> SingleEventHandler(
    singleEvent: SingleEvent<T>,
    handler: (T) -> Unit
) {
    LaunchedEffect(singleEvent) {
        singleEvent.flow.collect {
            handler(it)
        }
    }
}