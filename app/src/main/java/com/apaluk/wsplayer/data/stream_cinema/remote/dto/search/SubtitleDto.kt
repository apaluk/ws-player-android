package com.apaluk.wsplayer.data.stream_cinema.remote.dto.search


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubtitleDto(
    @Json(name = "language")
    val language: String?,
    @Json(name = "src")
    val src: String?
)