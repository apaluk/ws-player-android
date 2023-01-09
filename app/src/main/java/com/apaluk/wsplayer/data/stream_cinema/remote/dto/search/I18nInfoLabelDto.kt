package com.apaluk.wsplayer.data.stream_cinema.remote.dto.search


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class I18nInfoLabelDto(
    @Json(name = "art")
    val art: ArtDto?,
    @Json(name = "lang")
    val lang: String?,
    @Json(name = "parent_titles")
    val parentTitles: List<String>?,
    @Json(name = "plot")
    val plot: String?,
    @Json(name = "plotoutline")
    val plotoutline: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "trailer")
    val trailer: String?
)