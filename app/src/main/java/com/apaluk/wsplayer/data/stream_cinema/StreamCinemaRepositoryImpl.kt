package com.apaluk.wsplayer.data.stream_cinema

import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.core.util.repositoryFlow
import com.apaluk.wsplayer.data.stream_cinema.remote.StreamCinemaApi
import com.apaluk.wsplayer.data.stream_cinema.remote.mapper.*
import com.apaluk.wsplayer.domain.model.media.TvShowEpisode
import com.apaluk.wsplayer.domain.model.media.TvShowSeason
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

    override fun getTvShowSeasons(mediaId: String): Flow<Resource<List<TvShowSeason>>> = repositoryFlow(
        apiOperation = { streamCinemaApi.getTvShowSeasons(mediaId = mediaId) },
        resultMapping = { it.toListOfSeasons() }
    )

    override fun getTvShowSeasonEpisodes(mediaId: String): Flow<Resource<List<TvShowEpisode>>> = repositoryFlow(
        apiOperation = { streamCinemaApi.getTvShowSeasonEpisodes(mediaId = mediaId) },
        resultMapping = { it.toListOfEpisodes() }
    )
}