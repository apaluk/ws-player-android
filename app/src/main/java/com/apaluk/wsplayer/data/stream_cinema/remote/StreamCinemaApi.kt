package com.apaluk.wsplayer.data.stream_cinema.remote

import com.apaluk.wsplayer.data.stream_cinema.remote.dto.MediaFilterResponseDto
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.streams.MediaStreamsResponseItemDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StreamCinemaApi {

    @GET("/api/media/filter/custom")
    suspend fun mediaFilter(
        @Query("access_token") accessToken: String,
        @Query("config") filterConfig: String,
        @Query("sortConfig") sortConfig: String
    ): Response<MediaFilterResponseDto>

    @GET("/api/media/{id}")
    suspend fun mediaDetails(
        @Query("access_token") accessToken: String,
        @Path("id") mediaId: String,
    )

    @GET("/api/media/{id}/streams")
    suspend fun getStreams(
        @Path("id") mediaId: String,
        @Query("access_token") accessToken: String,
    ): Response<List<MediaStreamsResponseItemDto>>

}