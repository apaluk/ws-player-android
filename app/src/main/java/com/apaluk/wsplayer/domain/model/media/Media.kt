package com.apaluk.wsplayer.domain.model.media

data class Media(
    val id: String,
    val name: String
)

data class MediaStream(
    val id: String,
    val ident: String,
    val audios: List<String>,
    val videos: List<String>
)