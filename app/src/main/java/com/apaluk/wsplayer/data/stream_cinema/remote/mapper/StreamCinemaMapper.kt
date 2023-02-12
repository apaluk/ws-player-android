package com.apaluk.wsplayer.data.stream_cinema.remote.mapper

import com.apaluk.wsplayer.core.util.requireNotNullOrEmpty
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.media.MediaDetailDto
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.search.HitDto
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.search.I18nInfoLabelDto
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.search.SearchResponseDto
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.streams.MediaStreamsResponseItemDto
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.streams.SubtitleDto
import com.apaluk.wsplayer.data.stream_cinema.remote.dto.streams.VideoDto
import com.apaluk.wsplayer.domain.model.media.*
import kotlin.math.roundToInt

fun SearchResponseDto.toSearchResultItems(): List<SearchResultItem> =
    hits.hits.mapNotNull { it.toSearchResultItem() }

fun HitDto.toSearchResultItem(): SearchResultItem? =
    try {
        SearchResultItem(
            id = id,
            title = getTitle(),
            originalTitle = source.infoLabels.originaltitle,
            year = source.infoLabels.year?.toString().orEmpty(),
            genre = source.infoLabels.genre,
            duration = source.infoLabels.duration ?: 0,
            cast = source.cast.map { it.name },
            director = source.infoLabels.director.orEmpty(),
            imageUrl = getImageUrl()
        )
    } catch (e: Exception) {
        null
    }

fun MediaDetailDto.toMediaDetail(): MediaDetail =
    MediaDetail(
        id = id,
        title = getTitle(),
        originalTitle = infoLabels.originaltitle,
        year = infoLabels.year.toString(),
        directors = infoLabels.director,
        writer = infoLabels.writer,
        cast = cast.map { it.name },
        genre = infoLabels.genre,
        plot = getPlot(),
        imageUrl = getImageUrl(),
        duration = streamInfo.video?.duration?.roundToInt() ?: infoLabels.duration
    )

fun MediaStreamsResponseItemDto.toMediaStream(): MediaStream {
    val duration = video.firstOrNull()?.duration?.toInt() ?: 0
    return MediaStream(
        ident = ident,
        size = size,
        duration = duration,
        speed = if (duration == 0) 0.0 else size.toDouble() / duration.toDouble(),
        audios = audio.map { it.language }.filter { it.isNotBlank() }.distinct(),
        video = video.firstOrNull()?.toVideoDefinition() ?: VideoDefinition.SD,
        subtitles = subtitles.mapNotNull { it.toSubtitles() }
    )
}

private fun HitDto.getImageUrl(): String? =
    source.i18nInfoLabels.getPosterImageUrl("en")
        ?: source.i18nInfoLabels.getPosterImageUrl("sk")
        ?: source.i18nInfoLabels.getPosterImageUrl("cs")

private fun MediaDetailDto.getImageUrl(): String? =
    i18nInfoLabels.getFanArtImageUrl("en")
        ?: i18nInfoLabels.getFanArtImageUrl("sk")
        ?: i18nInfoLabels.getFanArtImageUrl("cs")

private fun List<I18nInfoLabelDto>.getPosterImageUrl(lang: String): String? =
    this.firstOrNull { it.lang == lang }
        ?.art?.poster

private fun List<I18nInfoLabelDto>.getFanArtImageUrl(lang: String): String? =
    this.firstOrNull { it.lang == lang }
        ?.art?.fanart

private fun HitDto.getTitle(): String =
    source.i18nInfoLabels.getTitle("sk")
        ?: source.i18nInfoLabels.getTitle("cs")
        ?: source.i18nInfoLabels.getTitle("en")
        ?: requireNotNull(source.infoLabels.originaltitle)

private fun MediaDetailDto.getTitle(): String =
    i18nInfoLabels.getTitle("sk")
        ?: i18nInfoLabels.getTitle("cs")
        ?: i18nInfoLabels.getTitle("en")
        ?: requireNotNull(infoLabels.originaltitle)

private fun MediaDetailDto.getPlot(): String =
    i18nInfoLabels.getPlot("sk")
        ?: i18nInfoLabels.getPlot("cs")
        ?: i18nInfoLabels.getPlot("en").orEmpty()

private fun List<I18nInfoLabelDto>.getTitle(lang: String): String? =
    firstOrNull { it.lang == lang }
        ?.title.run {
            // if it's blank, return null
            if(this.isNullOrBlank()) null else this
        }

private fun List<I18nInfoLabelDto>.getPlot(lang: String): String? =
    firstOrNull { it.lang == lang }
        ?.plot.run {
            // if it's blank, return null
            if(this.isNullOrBlank()) null else this
        }

private fun VideoDto.toVideoDefinition(): VideoDefinition =
    when {
        height > 2500 -> VideoDefinition.UHD_8K
        height > 2000 -> VideoDefinition.UHD_4K
        height > 900 -> VideoDefinition.FHD
        height > 600 -> VideoDefinition.HD
        else -> VideoDefinition.SD
    }

private fun SubtitleDto.toSubtitles(): Subtitles? =
    try {
        Subtitles(
            lang = requireNotNullOrEmpty(language),
            forced = forced ?: false
        )
    } catch (e: Exception) {
        null
    }