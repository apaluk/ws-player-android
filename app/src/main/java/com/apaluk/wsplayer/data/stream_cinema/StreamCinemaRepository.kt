package com.apaluk.wsplayer.data.stream_cinema

import com.apaluk.wsplayer.core.util.repositoryFlow
import com.apaluk.wsplayer.data.stream_cinema.remote.StreamCinemaApi
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.*
import com.apaluk.wsplayer.data.stream_cinema.remote.mapper.toMedia
import com.apaluk.wsplayer.data.stream_cinema.remote.mapper.toMediaStream
import com.squareup.moshi.Moshi
import javax.inject.Inject

class StreamCinemaRepository @Inject constructor(
    private val streamCinemaApi: StreamCinemaApi
) {

    fun mediaFilter() = repositoryFlow(
        apiOperation = {
            val moshi = Moshi.Builder().build()
            val filterConfig = FilterConfigDtoJsonAdapter(moshi).toJson(
                FilterConfigDto(
                    BoolDto(
                        listOf(
                            MustDto(
                                mapOf(
                                    "cast.name" to "Tom Cruise"
                                )
                            )
                        )
                    )
                )
            )
            val sortConfig = SortConfigDtoJsonAdapter(moshi).toJson(
                SortConfigDto(
                    dateAdded =  SortFieldDto(SortOrderDto.asc)
                )
            )
            streamCinemaApi.mediaFilter(
                filterConfig = filterConfig,
                sortConfig = sortConfig
            )
        },
        resultMapping = {
            it.mediaFilterData.map { mediaData -> mediaData.toMedia() }
        }
    )

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
        resultMapping = {}
    )

}