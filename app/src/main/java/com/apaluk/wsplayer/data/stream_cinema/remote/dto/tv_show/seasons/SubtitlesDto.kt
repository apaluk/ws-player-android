package com.apaluk.wsplayer.data.stream_cinema.remote.dto.tv_show.seasons


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubtitlesDto(
    @Json(name = "items")
    val items: List<ItemDto>,
    @Json(name = "map")
    val map: List<String>
)