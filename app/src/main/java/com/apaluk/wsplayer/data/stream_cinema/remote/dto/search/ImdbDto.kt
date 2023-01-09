package com.apaluk.wsplayer.data.stream_cinema.remote.dto.search


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImdbDto(
    @Json(name = "rating")
    val rating: Double,
    @Json(name = "votes")
    val votes: Int
)