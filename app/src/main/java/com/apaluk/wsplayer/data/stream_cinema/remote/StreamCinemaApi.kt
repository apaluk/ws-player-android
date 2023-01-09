package com.apaluk.wsplayer.data.stream_cinema.remote

import com.apaluk.wsplayer.core.util.Constants
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.MediaFilterResponseDto
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.search.SearchResponseDto
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.streams.MediaStreamsResponseItemDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StreamCinemaApi {

    @GET("/api/media/filter/custom")
    suspend fun mediaFilter(
        @Query("access_token") accessToken: String = Constants.STREAM_CINEMA_ACCESS_TOKEN,
        @Query("config") filterConfig: String,
        @Query("sortConfig") sortConfig: String
    ): Response<MediaFilterResponseDto>

    @GET("/api/media/{id}")
    suspend fun mediaDetails(
        @Query("access_token") accessToken: String = Constants.STREAM_CINEMA_ACCESS_TOKEN,
        @Path("id") mediaId: String,
    )

    @GET("/api/media/{id}/streams")
    suspend fun getStreams(
        @Path("id") mediaId: String,
        @Query("access_token") accessToken: String = Constants.STREAM_CINEMA_ACCESS_TOKEN,
    ): Response<List<MediaStreamsResponseItemDto>>


    @GET("/api/media/filter/search")
    suspend fun search(
        @Query("access_token") accessToken: String = Constants.STREAM_CINEMA_ACCESS_TOKEN,
        @Query("type") type: String = "*",
        @Query("order") order: String = "desc",
        @Query("value") searchText: String
    ): Response<SearchResponseDto>

}