package com.apaluk.wsplayer.data.stream_cinema.remote.dto.search


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollectionDto(
    @Json(name = "art")
    val art: ArtDto,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String
)