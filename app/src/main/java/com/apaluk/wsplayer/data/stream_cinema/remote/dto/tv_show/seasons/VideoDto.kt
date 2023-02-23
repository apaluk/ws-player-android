package com.apaluk.wsplayer.data.stream_cinema.remote.dto.tv_show.seasons


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoDto(
    @Json(name = "lang")
    val lang: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "size")
    val size: Int?,
    @Json(name = "subtitles")
    val subtitles: List<String>?,
    @Json(name = "type")
    val type: String,
    @Json(name = "url")
    val url: String
)