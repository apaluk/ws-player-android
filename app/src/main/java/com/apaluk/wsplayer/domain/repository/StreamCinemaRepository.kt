package com.apaluk.wsplayer.domain.repository

import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.domain.model.media.*
import kotlinx.coroutines.flow.Flow

interface StreamCinemaRepository {

    fun getMediaStreams(mediaId: String): Flow<Resource<List<MediaStream>>>
    fun search(text: String): Flow<Resource<List<SearchResultItem>>>
    fun getMediaDetails(mediaId: String): Flow<Resource<MediaDetail>>
    fun getTvShowSeasons(mediaId: String): Flow<Resource<List<TvShowSeason>>>
    fun getTvShowSeasonEpisodes(mediaId: String): Flow<Resource<List<TvShowEpisode>>>
}