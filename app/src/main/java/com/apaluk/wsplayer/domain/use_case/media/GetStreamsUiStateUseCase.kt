package com.apaluk.wsplayer.domain.use_case.media

import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import com.apaluk.wsplayer.domain.repository.WatchHistoryRepository
import com.apaluk.wsplayer.ui.media_detail.StreamsUiState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetStreamsUiStateUseCase @Inject constructor(
    private val streamCinemaRepository: StreamCinemaRepository,
    private val watchHistoryRepository: WatchHistoryRepository
) {
    operator fun invoke(mediaId: String): Flow<StreamsUiState?> = flow {
        val streams = streamCinemaRepository.getMediaStreams(mediaId).last().data?.sortedBy { stream -> stream.size }
            ?: run {
                emit(null)
                return@flow
            }
        emitAll(
            watchHistoryRepository.getMediaWatchHistory(mediaId)
                .map { it.firstOrNull() }
                .distinctUntilChanged()
                .map {
                    StreamsUiState(
                        streams = streams,
                        selectedStreamId = watchHistoryRepository.getStreamIdent(it?.streamId)
                    )
                }
        )
    }
}

private suspend fun WatchHistoryRepository.getStreamIdent(streamId: Long?): String? =
    if (streamId == null) null else getStreamIdent(streamId)
