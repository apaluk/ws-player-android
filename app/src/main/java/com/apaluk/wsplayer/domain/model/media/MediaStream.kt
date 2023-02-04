package com.apaluk.wsplayer.domain.model.media

data class MediaStream(
    val ident: String,
    val size: Long,
    val duration: Int,
    val speed: Double,
    val video: VideoDefinition,
    val audios: List<String>,
    val subtitles: List<Subtitles>
)

data class Subtitles(
    val lang: String,
    val forced: Boolean
)

enum class VideoDefinition {
    SD, HD, FHD, U4K, U8K;

    override fun toString(): String =
        when(this) {
            SD -> "SD"
            HD -> "HD"
            FHD -> "FHD"
            U4K -> "4K"
            U8K -> "8K"
        }
}

fun VideoDefinition.height(): Int =
    when(this) {
        VideoDefinition.SD -> 480
        VideoDefinition.HD -> 720
        VideoDefinition.FHD -> 1080
        VideoDefinition.U4K -> 2160
        VideoDefinition.U8K -> 4320
    }

val DUMMY_MEDIA_STREAMS = listOf(
    MediaStream(
        ident = "",
        size = 12345689000L,
        duration = 5420,
        speed = 1204.5,
        video = VideoDefinition.HD,
        audios = listOf("CZ"),
        subtitles = listOf(Subtitles("EN", true))
    ),
    MediaStream(
        ident = "",
        size = 34545689000L,
        duration = 5420,
        speed = 1204.5,
        video = VideoDefinition.FHD,
        audios = emptyList(),
        subtitles = listOf(Subtitles("EN", true), Subtitles("SK", false))
    )
)