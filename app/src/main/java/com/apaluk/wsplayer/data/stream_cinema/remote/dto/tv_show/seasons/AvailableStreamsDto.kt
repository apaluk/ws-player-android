package com.apaluk.wsplayer.data.stream_cinema.remote.dto.tv_show.seasons


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AvailableStreamsDto(
    @Json(name = "count")
    val count: Int,
    @Json(name = "languages")
    val languages: LanguagesDto
)