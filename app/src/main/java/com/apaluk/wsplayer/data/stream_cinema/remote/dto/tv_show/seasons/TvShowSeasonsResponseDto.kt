package com.apaluk.wsplayer.data.stream_cinema.remote.dto.tv_show.seasons


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TvShowSeasonsResponseDto(
    @Json(name = "hits")
    val hits: HitsDto,
    @Json(name = "_shards")
    val shards: ShardsDto,
    @Json(name = "timed_out")
    val timedOut: Boolean,
    @Json(name = "took")
    val took: Int
)