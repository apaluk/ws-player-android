package com.apaluk.wsplayer.domain.model.dashboard.util

import com.apaluk.wsplayer.domain.model.dashboard.DashboardMedia

val DashboardMedia.relativeProgress: Float?
    get() {
        return if(progressSeconds == null || duration == null || duration == 0) null
        else progressSeconds.toFloat() / duration.toFloat()
    }

val DashboardMedia.isEmpty: Boolean
    get() = mediaId == null