package com.apaluk.wsplayer.data.stream_cinema

import com.apaluk.wsplayer.core.util.repositoryFlow
import com.apaluk.wsplayer.data.stream_cinema.remote.StreamCinemaApi
import com.apaluk.wsplayer.data.stream_cinema.remote.mapper.toMediaDetail
import com.apaluk.wsplayer.data.stream_cinema.remote.mapper.toMediaStream
import com.apaluk.wsplayer.data.stream_cinema.remote.mapper.toSearchResultItems
import javax.inject.Inject

class StreamCinemaRepository @Inject constructor(
    private val streamCinemaApi: StreamCinemaApi
) {
    fun getMediaStreams(mediaId: String) = repositoryFlow(
        apiOperation = {
            streamCinemaApi.getStreams(
                mediaId = mediaId
            )
        },
        resultMapping = { it.map { item -> item.toMediaStream() } }
    )

    fun search(text: String) = repositoryFlow(
        apiOperation = {
            streamCinemaApi.search(
                searchText = text
            )
        },
        resultMapping = { it.toSearchResultItems() }
    )

    fun getMediaDetails(mediaId: String) = repositoryFlow(
        apiOperation = {
            streamCinemaApi.mediaDetails(mediaId = mediaId)
        },
        resultMapping = { it.toMediaDetail() }
    )
}