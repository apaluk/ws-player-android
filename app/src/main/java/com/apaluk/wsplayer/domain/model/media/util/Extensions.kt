package com.apaluk.wsplayer.domain.model.media.util

import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.core.util.convertNonSuccess
import com.apaluk.wsplayer.data.database.model.MediaInfo
import com.apaluk.wsplayer.data.database.model.Stream
import com.apaluk.wsplayer.data.database.model.WatchHistory
import com.apaluk.wsplayer.domain.model.dashboard.DashboardMedia
import com.apaluk.wsplayer.domain.model.media.MediaBrief
import com.apaluk.wsplayer.domain.model.media.MediaDetail
import com.apaluk.wsplayer.domain.model.media.MediaDetailMovie
import com.apaluk.wsplayer.domain.model.media.MediaDetailTvShow
import com.apaluk.wsplayer.domain.model.media.MediaProgress
import com.apaluk.wsplayer.domain.model.media.MediaStream
import com.apaluk.wsplayer.domain.model.media.TvShowChild
import com.apaluk.wsplayer.domain.model.media.TvShowEpisode
import com.apaluk.wsplayer.domain.model.media.TvShowSeason
import com.apaluk.wsplayer.domain.model.media.VideoDefinition
import com.apaluk.wsplayer.domain.model.media.WatchHistoryEntry

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

internal fun Resource<List<TvShowChild>>.tryGetSeasons(): Resource<List<TvShowSeason>?> {
    if(this !is Resource.Success) return this.convertNonSuccess()
    val seasons = data?.filterIsInstance<TvShowSeason>() ?: return Resource.Success(null)
    return if(seasons.isEmpty()) Resource.Success(null) else Resource.Success(seasons)
}

internal fun Resource<List<TvShowChild>>.tryGetEpisodes(): Resource<List<TvShowEpisode>?> {
    if(this !is Resource.Success) return this.convertNonSuccess()
    val episodes = data?.filterIsInstance<TvShowEpisode>() ?: return Resource.Success(null)
    return if(episodes.isEmpty()) return Resource.Success(null) else Resource.Success(episodes)
}

fun TvShowChild.orderNumber(): Int = when(this) {
    is TvShowSeason -> orderNumber
    is TvShowEpisode -> orderNumber
}
