package com.apaluk.wsplayer.data.stream_cinema.remote.dto.tv_show.seasons


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoDto(
    @Json(name = "aspect")
    val aspect: Double?,
    @Json(name = "codec")
    val codec: String?,
    @Json(name = "duration")
    val duration: Double?,
    @Json(name = "height")
    val height: Int?,
    @Json(name = "width")
    val width: Int?
)