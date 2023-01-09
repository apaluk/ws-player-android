package com.apaluk.wsplayer.data.stream_cinema.remote.dto.search


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StreamInfoDto(
    @Json(name = "audio")
    val audio: AudioDtoX?,
    @Json(name = "subtitles")
    val subtitles: SubtitlesDtoX?,
    @Json(name = "video")
    val video: VideoDto?
)