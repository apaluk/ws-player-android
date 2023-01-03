package com.apaluk.wsplayer.data.stream_cinema.remote.mapper

import com.apaluk.wsplayer.data.stream_cinema.remote.dto.MediaDataDto
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.streams.MediaStreamsResponseItemDto
import com.apaluk.wsplayer.domain.model.media.Media
import com.apaluk.wsplayer.domain.model.media.MediaStream

fun MediaDataDto.toMedia(): Media =
    Media(
        id = id,
        name = source.infoLabels.originalTitle.orEmpty()
    )

fun MediaStreamsResponseItemDto.toMediaStream(): MediaStream =
    MediaStream(
        id = id,
        ident = ident,
        audios = audio.map { "${it.language} (${it.codec})" },
        videos = video.map { it.codec }
    )