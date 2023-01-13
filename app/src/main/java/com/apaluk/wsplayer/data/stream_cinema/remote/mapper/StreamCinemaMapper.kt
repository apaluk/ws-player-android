package com.apaluk.wsplayer.data.stream_cinema.remote.mapper

import com.apaluk.wsplayer.data.stream_cinema.remote.dto.MediaDataDto
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.search.SearchResponseDto
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.search.SearchResultItemDto
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.streams.MediaStreamsResponseItemDto
import com.apaluk.wsplayer.domain.model.media.Media
import com.apaluk.wsplayer.domain.model.media.MediaStream
import com.apaluk.wsplayer.domain.model.media.SearchResultItem

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

fun SearchResponseDto.toSearchResultItems(): List<SearchResultItem> =
    searchResultItems.mapNotNull { it.toSearchResultItem() }

fun SearchResultItemDto.toSearchResultItem(): SearchResultItem? =
    try {
        SearchResultItem(
            id = id,
            title = getTitle(),
            originalTitle = source.infoLabels.originaltitle,
            year = source.infoLabels.year.toString(),
            genre = source.infoLabels.genre,
            duration = source.infoLabels.duration ?: 0,
            cast = source.cast.map { it.name },
            director = source.infoLabels.director.orEmpty(),
            imageUrl = getImageUrl()
        )
    } catch (e: Exception) {
        null
    }

private fun SearchResultItemDto.getImageUrl(): String? =
    getPosterImageUrl("en") ?: getPosterImageUrl("sk") ?: getPosterImageUrl("cs")

private fun SearchResultItemDto.getPosterImageUrl(lang: String): String? =
    source.i18nInfoLabels
        .firstOrNull { it.lang == lang }
        ?.art?.poster

private fun SearchResultItemDto.getTitle(): String =
    getTitle("sk") ?: getTitle("cs") ?: getTitle("en") ?: requireNotNull(source.infoLabels.originaltitle)

private fun SearchResultItemDto.getTitle(lang: String): String? =
    source.i18nInfoLabels
        .firstOrNull { it.lang == lang }
        ?.title.run {
            // if it's blank, return null
            if(this.isNullOrBlank()) null else this
        }