package com.apaluk.wsplayer.data.stream_cinema.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StreamInfoDto(
    @Json(name = "audio")
    val audio: AudioDto,
    @Json(name = "subtitles")
    val subtitles: SubtitlesDto?,
    @Json(name = "video")
    val video: VideoDto
)