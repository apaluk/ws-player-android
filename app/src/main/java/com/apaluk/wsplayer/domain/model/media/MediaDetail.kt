package com.apaluk.wsplayer.domain.model.media

data class MediaDetail(
    val id: String,
    val title: String,
    val originalTitle: String?,
    val year: String?,
    val directors: List<String>,
    val writer: List<String>,
    val cast: List<String>,
    val genre: List<String>,
    val plot: String?,
    val imageUrl: String?,
    val duration: Int
)

data class TvShowSeason(
    val seasonNumber: Int,
    val year: String?,
    val directors: List<String>,
    val writer: List<String>,
    val cast: List<String>,
    val genre: List<String>,
    val plot: String?,
    val imageUrl: String?,
)

data class TvShowEpisode(
    val episodeNumber: Int,
    val seasonNumber: Int,
    val year: String?,
    val directors: List<String>,
    val writer: List<String>,
    val cast: List<String>,
    val genre: List<String>,
    val plot: String?,
    val imageUrl: String?,
    val duration: Int
)