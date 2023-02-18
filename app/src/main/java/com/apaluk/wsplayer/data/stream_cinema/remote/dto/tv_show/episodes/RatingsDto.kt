package com.apaluk.wsplayer.data.stream_cinema.remote.dto.tv_show.episodes


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RatingsDto(
    @Json(name = "tmdb")
    val tmdb: TmdbDto,
    @Json(name = "trakt")
    val trakt: TraktDto
)