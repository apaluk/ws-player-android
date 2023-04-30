package com.apaluk.wsplayer.domain.use_case.dashboard

import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.domain.model.dashboard.DashboardMedia
import com.apaluk.wsplayer.domain.model.media.MediaDetailMovie
import com.apaluk.wsplayer.domain.model.media.MediaDetailTvShow
import com.apaluk.wsplayer.domain.model.media.util.toDashboardMedia
import com.apaluk.wsplayer.domain.repository.MediaInfoRepository
import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import com.apaluk.wsplayer.domain.repository.WatchHistoryRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetContinueWatchingMediaListUseCase @Inject constructor(
    private val watchHistoryRepository: WatchHistoryRepository,
    private val streamCinemaRepository: StreamCinemaRepository,
    private val mediaInfoRepository: MediaInfoRepository
) {

    operator fun invoke(): Flow<Resource<List<DashboardMedia>>> = flow {
        emit(Resource.Loading())
        val lastInProgressMedia = watchHistoryRepository.getLastInProgressMedia().first()
        val continueWatchingLocal = lastInProgressMedia.map {
            mediaInfoRepository.getMediaInfo(it.mediaId)?.toDashboardMedia() ?: DashboardMedia()
        }
        emit(Resource.Success(continueWatchingLocal))
        emitAll(watchHistoryRepository.getLastInProgressMedia()
            .map { list ->
                list.map mapList@{
                    val mediaDetail = streamCinemaRepository.getMediaDetails(it.mediaId).last().data
                        ?: return@mapList null

                    when (mediaDetail) {
                        is MediaDetailMovie -> {
                            DashboardMedia(
                                mediaId = it.mediaId,
                                title = mediaDetail.title,
                                duration = mediaDetail.duration,
                                progressSeconds = it.progressSeconds,
                                imageUrl = mediaDetail.imageUrl
                            )
                        }
                        is MediaDetailTvShow -> {
                            DashboardMedia(
                                mediaId = it.mediaId,
                                title = mediaDetail.title,
                                duration = mediaDetail.duration,
                                progressSeconds = it.progressSeconds,
                                imageUrl = mediaDetail.imageUrl
                            )
                        }
                    }
                }.filterNotNull()
            }.map { Resource.Success(it) }
        )
    }
}