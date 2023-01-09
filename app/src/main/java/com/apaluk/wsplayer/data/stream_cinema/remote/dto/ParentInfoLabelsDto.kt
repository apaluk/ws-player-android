package com.apaluk.wsplayer.data.stream_cinema.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ParentInfoLabelsDto(
    @Json(name = "originaltitle")
    val originaltitle: List<String>?
)