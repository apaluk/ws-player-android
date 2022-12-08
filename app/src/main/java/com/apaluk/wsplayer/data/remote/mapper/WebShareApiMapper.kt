package com.apaluk.wsplayer.data.remote.mapper

import com.apaluk.wsplayer.data.remote.dto.StatusDto

fun String.toStatusDto() = when(this) {
    "OK" -> StatusDto.OK
    "FATAL" -> StatusDto.Fatal
    else -> StatusDto.Unknown
}