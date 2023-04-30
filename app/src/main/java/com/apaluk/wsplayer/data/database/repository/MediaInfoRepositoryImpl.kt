package com.apaluk.wsplayer.data.database.repository

import com.apaluk.wsplayer.data.database.dao.MediaInfoDao
import com.apaluk.wsplayer.domain.model.media.MediaBrief
import com.apaluk.wsplayer.domain.model.media.util.toMediaBrief
import com.apaluk.wsplayer.domain.model.media.util.toMediaInfo
import com.apaluk.wsplayer.domain.repository.MediaInfoRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MediaInfoRepositoryImpl @Inject constructor(
    private val mediaInfoDao: MediaInfoDao
): MediaInfoRepository {
    override suspend fun getMediaInfo(mediaId: String): MediaBrief? {
        return mediaInfoDao.getMediaInfo(mediaId).first()?.toMediaBrief()
    }

    override suspend fun upsertMediaInfo(mediaBrief: MediaBrief) {
        val mediaInfo = mediaInfoDao.getMediaInfo(mediaBrief.mediaId).first()?.copy(
            title = mediaBrief.title,
            imageUrl = mediaBrief.imageUrl,
            durationSeconds = mediaBrief.durationSeconds
        ) ?: mediaBrief.toMediaInfo()
        mediaInfoDao.upsertMediaInfo(mediaInfo)
    }
}