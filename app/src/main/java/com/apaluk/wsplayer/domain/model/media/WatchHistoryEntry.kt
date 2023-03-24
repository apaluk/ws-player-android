package com.apaluk.wsplayer.domain.model.media

import androidx.room.PrimaryKey

data class WatchHistoryEntry(
    @PrimaryKey val id: Long,
    val mediaId: String,
    val seasonId: String?,
    val episodeId: String?,
    val streamId: Long,
    val progressSeconds: Int,
    val lastUpdate: Long,
    val isWatched: Boolean
)

fun WatchHistoryEntry.toMediaProgress(): MediaProgress =
    MediaProgress(
        progressSeconds = progressSeconds,
        isWatched = isWatched
    )