package com.apaluk.wsplayer.domain.repository

import com.apaluk.wsplayer.domain.model.media.MediaBrief

interface MediaInfoRepository {
    suspend fun getMediaInfo(mediaId: String): MediaBrief?
    suspend fun upsertMediaInfo(mediaBrief: MediaBrief)
}