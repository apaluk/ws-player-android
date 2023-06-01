package com.apaluk.wsplayer.data.stream_cinema.remote.dto.tv_show.seasons


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AudioDtoX(
    @Json(name = "channels")
    val channels: Int,
    @Json(name = "codec")
    val codec: String,
    @Json(name = "language")
    val language: String?
)