package com.apaluk.wsplayer.data.stream_cinema.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilterConfigDto(
    val bool: BoolDto
)

@JsonClass(generateAdapter = true)
data class BoolDto(
    val must: List<MustDto>
)

@JsonClass(generateAdapter = true)
data class MustDto(
    val match: Map<String, String>
)

@JsonClass(generateAdapter = true)
data class SortConfigDto(
    @Json(name = "ratings.dateadded")
    val dateAdded: SortFieldDto
)

@JsonClass(generateAdapter = true)
data class SortFieldDto(
    val order: SortOrderDto
)

enum class SortOrderDto {
    asc, desc
}