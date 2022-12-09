package com.apaluk.wsplayer.core.util

import kotlinx.coroutines.CancellationException

fun Exception.throwIfCancellation() {
    if(this is CancellationException)
        throw this
}