package com.apaluk.wsplayer.data.stream_cinema.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MediaFilterResponseDto(
    @Json(name = "data")
    val mediaFilterData: List<MediaDataDto>,
    @Json(name = "totalCount")
    val totalCount: Int
)