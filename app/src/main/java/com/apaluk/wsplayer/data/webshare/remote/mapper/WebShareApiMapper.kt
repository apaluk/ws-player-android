package com.apaluk.wsplayer.data.webshare.remote.mapper

import com.apaluk.wsplayer.data.webshare.remote.dto.StatusDto

fun String.toStatusDto() = when(this) {
    "OK" -> StatusDto.OK
    "FATAL" -> StatusDto.Fatal
    else -> StatusDto.Unknown
}