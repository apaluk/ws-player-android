package com.apaluk.wsplayer.domain.use_case.media

import com.apaluk.wsplayer.domain.model.media.MediaStream
import com.apaluk.wsplayer.domain.repository.WatchHistoryRepository
import javax.inject.Inject

class UpdateWatchHistoryOnStartStreamUseCase @Inject constructor(
    private val watchHistoryRepository: WatchHistoryRepository
) {
    /** Returns new or updated watch history ID. */
    suspend operator fun invoke(
        stream: MediaStream,
        mediaId: String,
        season: String?,
        episode: String?
    ): Long {
        val streamId = watchHistoryRepository.ensureStream(stream)
        return watchHistoryRepository.ensureWatchHistoryEntry(mediaId, season, episode, streamId)
    }
}