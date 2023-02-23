package com.apaluk.wsplayer.domain.use_case.media

import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.domain.model.media.MediaDetail
import com.apaluk.wsplayer.domain.model.media.MediaDetailTvShow
import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class GetMediaDetailUseCase @Inject constructor(
    private val streamCinemaRepository: StreamCinemaRepository
) {
    operator fun invoke(mediaId: String): Flow<Resource<MediaDetail>> = flow {
        emit(Resource.Loading())
        val mediaDetailsResource = streamCinemaRepository.getMediaDetails(mediaId).last()
        emit(mediaDetailsResource)

        // download seasons for TV show
        if(mediaDetailsResource is Resource.Success && mediaDetailsResource.data is MediaDetailTvShow) {
            val seasonsResource = streamCinemaRepository.getTvShowSeasons(mediaId).last()
            if(seasonsResource is Resource.Success) {
                seasonsResource.data?.let { seasons ->
                    emit(Resource.Success(mediaDetailsResource.data.copy(
                        seasons = seasons
                    )))
                }
            } else if(seasonsResource is Resource.Error) {
                emit(Resource.Error(
                    message = seasonsResource.message,
                    exception = seasonsResource.exception,
                    data = mediaDetailsResource.data
                ))
            }
        }
    }

}