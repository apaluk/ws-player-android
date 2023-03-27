package com.apaluk.wsplayer.domain.use_case.media

import com.apaluk.wsplayer.domain.repository.WatchHistoryRepository
import javax.inject.Inject

class GetStartFromPositionUseCase @Inject constructor(
    private val watchHistoryRepository: WatchHistoryRepository
) {
    suspend operator fun invoke(id: Long): Int {
        val watchHistory = watchHistoryRepository.getWatchHistoryById(id)
        return if(watchHistory == null || watchHistory.isWatched) 0
            else watchHistory.progressSeconds
    }

}