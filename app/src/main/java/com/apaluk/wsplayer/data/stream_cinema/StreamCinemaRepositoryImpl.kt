package com.apaluk.wsplayer.data.stream_cinema

import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.core.util.repositoryFlow
import com.apaluk.wsplayer.data.stream_cinema.remote.StreamCinemaApi
import com.apaluk.wsplayer.data.stream_cinema.remote.mapper.*
import com.apaluk.wsplayer.domain.model.media.TvShowChild
import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StreamCinemaRepositoryImpl @Inject constructor(
    private val streamCinemaApi: StreamCinemaApi
): StreamCinemaRepository {
    override fun getMediaStreams(mediaId: String) = repositoryFlow(
        apiOperation = {
            streamCinemaApi.getStreams(
                mediaId = mediaId
            )
        },
        resultMapping = { it.map { item -> item.toMediaStream() } }
    )

    override fun search(text: String) = repositoryFlow(
        apiOperation = {
            streamCinemaApi.search(
                searchText = text
            )
        },
        resultMapping = { it.toSearchResultItems() }
    )

    override fun getMediaDetails(mediaId: String) = repositoryFlow(
        apiOperation = {
            streamCinemaApi.mediaDetails(mediaId = mediaId)
        },
        resultMapping = { it.toMediaDetail() }
    )

    override fun getTvShowChildren(mediaId: String): Flow<Resource<List<TvShowChild>>> = repositoryFlow(
        apiOperation = { streamCinemaApi.getMediaChildren(mediaId = mediaId) },
        resultMapping = { it.toListOfTvShowChildren() }
    )

}