package com.apaluk.wsplayer.data.stream_cinema.remote.dto.search


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LanguagesDto(
    @Json(name = "audio")
    val audio: AudioDto,
    @Json(name = "subtitles")
    val subtitles: SubtitlesDto
)