package com.apaluk.wsplayer.data.stream_cinema.remote.dto.search


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaginationDto(
    @Json(name = "from")
    val from: Int,
    @Json(name = "limit")
    val limit: Int,
    @Json(name = "next")
    val next: String,
    @Json(name = "page")
    val page: Int,
    @Json(name = "pageCount")
    val pageCount: Int,
    @Json(name = "prev")
    val prev: String
)