package com.apaluk.wsplayer.data.stream_cinema.remote.adapter

import com.apaluk.wsplayer.data.stream_cinema.remote.dto.media.MediaTypeDto
import com.squareup.moshi.FromJson
import timber.log.Timber

class MediaTypeAdapter {

    @FromJson
    fun fromJson(value: String): MediaTypeDto {
        return when(value) {
            "movie" -> MediaTypeDto.Movie
            "tvshow" -> MediaTypeDto.TvShow
            else -> {
                Timber.w("Unknown media type: $value")
                MediaTypeDto.Movie
            }
        }
    }
}