package com.apaluk.wsplayer.domain.repository

import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.domain.model.media.*
import com.apaluk.wsplayer.domain.model.search.SearchResultItem
import kotlinx.coroutines.flow.Flow

interface StreamCinemaRepository {

    fun getMediaStreams(mediaId: String): Flow<Resource<List<MediaStream>>>
    fun search(text: String): Flow<Resource<List<SearchResultItem>>>
    fun getMediaDetails(mediaId: String): Flow<Resource<MediaDetail>>
    /** Provides seasons or episodes, depending on API, since we don't know what will come from the API. */
    fun getTvShowChildren(mediaId: String): Flow<Resource<List<TvShowChild>>>
}