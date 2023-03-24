package com.apaluk.wsplayer.core.util

import java.util.concurrent.TimeUnit

fun Long.millisToSeconds(): Long = TimeUnit.MILLISECONDS.toSeconds(this)