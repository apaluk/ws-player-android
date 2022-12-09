package com.apaluk.wsplayer.core.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SingleEvent<T> {

    private val channel = Channel<T>()

    val flow
        get() = channel.receiveAsFlow()

    suspend fun emit(value: T) {
        channel.send(value)
    }
}