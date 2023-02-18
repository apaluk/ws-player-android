package com.apaluk.wsplayer.data.stream_cinema.remote.dto.tv_show.seasons


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InfoLabelsDto(
    @Json(name = "aired")
    val aired: String?,
    @Json(name = "country")
    val country: List<String>,
    @Json(name = "dateadded")
    val dateadded: String,
    @Json(name = "director")
    val director: List<String>,
    @Json(name = "duration")
    val duration: Int?,
    @Json(name = "genre")
    val genre: List<String>,
    @Json(name = "mediatype")
    val mediatype: String,
    @Json(name = "originaltitle")
    val originaltitle: String?,
    @Json(name = "premiered")
    val premiered: String?,
    @Json(name = "season")
    val season: Int,
    @Json(name = "studio")
    val studio: List<String>,
    @Json(name = "writer")
    val writer: List<String>,
    @Json(name = "year")
    val year: Int?
)