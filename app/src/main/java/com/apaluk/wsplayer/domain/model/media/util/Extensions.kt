package com.apaluk.wsplayer.domain.model.media.util

import com.apaluk.wsplayer.domain.model.media.MediaProgress
import com.apaluk.wsplayer.domain.model.media.WatchHistoryEntry


fun WatchHistoryEntry.toMediaProgress(): MediaProgress =
    MediaProgress(
        progressSeconds = progressSeconds,
        isWatched = isWatched
    )