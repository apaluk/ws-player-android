package com.apaluk.wsplayer.data.stream_cinema.remote.dto.search


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponseDto(
    @Json(name = "data")
    val `data`: List<DataDto>,
    @Json(name = "pagination")
    val pagination: PaginationDto,
    @Json(name = "totalCount")
    val totalCount: Int
)