package com.apaluk.wsplayer.domain.repository

import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.domain.model.media.MediaDetail
import com.apaluk.wsplayer.domain.model.media.MediaStream
import com.apaluk.wsplayer.domain.model.media.SearchResultItem
import kotlinx.coroutines.flow.Flow

interface StreamCinemaRepository {

    fun getMediaStreams(mediaId: String): Flow<Resource<List<MediaStream>>>
    fun search(text: String): Flow<Resource<List<SearchResultItem>>>
    fun getMediaDetails(mediaId: String): Flow<Resource<MediaDetail>>
}