package com.apaluk.wsplayer.domain.use_case.media

import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.domain.model.media.MediaDetail
import com.apaluk.wsplayer.domain.model.media.util.toMediaBrief
import com.apaluk.wsplayer.domain.repository.MediaInfoRepository
import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class GetMediaDetailsUseCase @Inject constructor(
    private val streamCinemaRepository: StreamCinemaRepository,
    private val mediaInfoRepository: MediaInfoRepository
) {

        suspend operator fun invoke(mediaId: String): Resource<MediaDetail> {
            val mediaDetailResource = streamCinemaRepository.getMediaDetails(mediaId).last()
            if(mediaDetailResource is Resource.Success && mediaDetailResource.data != null) {
                mediaInfoRepository.upsertMediaInfo(mediaDetailResource.data.toMediaBrief())
            }
            return mediaDetailResource
        }
}