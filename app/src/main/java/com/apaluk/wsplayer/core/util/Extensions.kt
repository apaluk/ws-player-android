package com.apaluk.wsplayer.core.util

fun requireNotNullOrEmpty(str: String?): String {
    require(!str.isNullOrEmpty())
    return str
}

fun Any?.isNullOrEmptyList(): Boolean {
    return this == null || (this is List<*> && isEmpty())
}