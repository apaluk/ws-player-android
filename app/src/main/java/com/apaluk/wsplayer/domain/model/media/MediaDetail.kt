package com.apaluk.wsplayer.domain.model.media

sealed class MediaDetail

data class MediaDetailMovie(
    val id: String,
    val title: String,
    val originalTitle: String?,
    val imageUrl: String?,
    val year: String?,
    val directors: List<String>,
    val writer: List<String>,
    val cast: List<String>,
    val genre: List<String>,
    val plot: String?,
    val duration: Int
): MediaDetail()

data class MediaDetailTvShow(
    val id: String,
    val title: String,
    val originalTitle: String?,
    val imageUrl: String?,
    val years: String?,
    val genre: List<String>,
    val plot: String?,
    val cast: List<String>,
    val numSeasons: Int,
): MediaDetail()

data class TvShowSeason(
    val id: String,
    val seasonNumber: Int,
    val name: String?,
    val year: String?,
    val directors: List<String>,
    val writer: List<String>,
    val cast: List<String>,
    val genre: List<String>,
    val plot: String?,
    val imageUrl: String?,
)

data class TvShowEpisode(
    val id: String,
    val episodeNumber: Int,
    val title: String?,
    val year: String?,
    val directors: List<String>,
    val writer: List<String>,
    val cast: List<String>,
    val genre: List<String>,
    val plot: String?,
    val imageUrl: String?,
    val thumbImageUrl: String?,
    val duration: Int
)
