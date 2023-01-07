package com.apaluk.wsplayer.core.util

fun requireNotNullOrEmpty(str: String?): String {
    require(!str.isNullOrEmpty())
    return str
}