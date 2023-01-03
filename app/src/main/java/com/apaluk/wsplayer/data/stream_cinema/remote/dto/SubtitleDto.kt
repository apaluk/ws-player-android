package com.apaluk.wsplayer.data.stream_cinema.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubtitleDto(
    @Json(name = "lang")
    val lang: String?,
    @Json(name = "src")
    val src: String?
)