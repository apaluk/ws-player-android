package com.apaluk.wsplayer.domain.model.media.util

import com.apaluk.wsplayer.data.database.model.MediaInfo
import com.apaluk.wsplayer.data.database.model.Stream
import com.apaluk.wsplayer.data.database.model.WatchHistory
import com.apaluk.wsplayer.domain.model.dashboard.DashboardMedia
import com.apaluk.wsplayer.domain.model.media.*

fun WatchHistoryEntry.toMediaProgress(): MediaProgress =
    MediaProgress(
        progressSeconds = progressSeconds,
        isWatched = isWatched
    )

fun WatchHistory.toWatchHistoryEntry(): WatchHistoryEntry =
    WatchHistoryEntry(id, mediaId, seasonId, episodeId, streamId, progressSeconds, lastUpdate, isWatched)

fun MediaStream.toStream(): Stream =
    Stream(
        id = 0L,
        ident = ident,
        definition = video.toInt(),
        speed = speed.toFloat(),
        language = audios.firstOrNull()
    )

private fun VideoDefinition.toInt() = ordinal

private fun Int.toVideoDefinition(): VideoDefinition =
    if(this in VideoDefinition.values().indices)
        VideoDefinition.values()[this]
    else
        VideoDefinition.SD

fun MediaInfo.toMediaBrief(): MediaBrief =
    MediaBrief(id, mediaId, title, imageUrl, durationSeconds)

fun MediaBrief.toMediaInfo(): MediaInfo =
    MediaInfo(id, mediaId, title, imageUrl, durationSeconds)

fun MediaBrief.toDashboardMedia(): DashboardMedia =
    DashboardMedia(
        mediaId = mediaId,
        title = title,
        duration = durationSeconds,
        imageUrl = imageUrl
    )

fun MediaDetail.toMediaBrief(): MediaBrief = when(this) {
    is MediaDetailMovie -> MediaBrief(0L, id, title, imageUrl, duration)
    is MediaDetailTvShow -> MediaBrief(0L, id, title, imageUrl, duration)
}