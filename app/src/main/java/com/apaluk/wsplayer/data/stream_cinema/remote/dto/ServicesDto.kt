package com.apaluk.wsplayer.data.stream_cinema.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServicesDto(
    @Json(name = "csfd")
    val csfd: String,
    @Json(name = "fanart")
    val fanart: String?,
    @Json(name = "imdb")
    val imdb: String?,
    @Json(name = "slug")
    val slug: String?,
    @Json(name = "tmdb")
    val tmdb: String?,
    @Json(name = "trakt")
    val trakt: String?,
    @Json(name = "trakt_with_type")
    val traktWithType: String?,
    @Json(name = "tvdb")
    val tvdb: String?
)