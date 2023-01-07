package com.apaluk.wsplayer.data.stream_cinema.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MediaDataDto(
    @Json(name = "_id")
    val id: String,
    @Json(name = "_score")
    val score: Double,
    @Json(name = "_source")
    val source: SourceDto
)