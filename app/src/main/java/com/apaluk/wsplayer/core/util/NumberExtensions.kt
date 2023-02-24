package com.apaluk.wsplayer.core.util

fun Int.withLeadingZeros(padding: Int): String =
    toString().padStart(padding, '0')