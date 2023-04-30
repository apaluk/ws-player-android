package com.apaluk.wsplayer.ui.media_detail.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.core.util.Constants
import com.apaluk.wsplayer.domain.model.media.*
import com.apaluk.wsplayer.ui.common.util.stringResourceSafe
import com.apaluk.wsplayer.ui.media_detail.*
import com.apaluk.wsplayer.ui.media_detail.tv_show.util.TvShowPosterData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

private const val MEDIA_INFO_SEPARATOR = "  ${Constants.CHAR_BULLET}  "

fun MediaDetail.toMediaDetailUiState(): MediaDetailUiState =
    when(this) {
        is MediaDetailMovie -> MovieMediaDetailUiState(movie = this)
        is MediaDetailTvShow -> TvShowMediaDetailUiState(
            tvShow = this,
            posterData = TvShowPosterData(imageUrl = imageUrl)
        )
    }

fun MediaDetailMovie.generalInfoText(): String {
    with(StringBuilder()) {
        year?.let {
            append(it).append(MEDIA_INFO_SEPARATOR)
        }
        if(genre.isNotEmpty()) {
            append(genre.joinToString(separator = " / "))
        }
        return toString()
    }
}

val MediaDetailMovie.relativeProgress: Float?
    get() = progress?.let { (it.progressSeconds.toFloat() / duration.toFloat()).coerceIn(0f, 1f) } ?: null

val MediaProgress.isInProgress: Boolean
    get() = progressSeconds > 0

val MediaDetailScreenUiState.tvShowUiState: TvShowMediaDetailUiState?
    get() = (mediaDetailUiState as? TvShowMediaDetailUiState)

fun MutableStateFlow<MediaDetailScreenUiState>.updateTvShowUiState(updateTvShowUiState: (TvShowMediaDetailUiState) -> TvShowMediaDetailUiState) {
    value.tvShowUiState?.let {
        update { mediaDetailUiState ->
            mediaDetailUiState.copy(mediaDetailUiState = updateTvShowUiState(it))
        }
    }
}

fun TvShowMediaDetailUiState.selectedSeason(): TvShowSeason? =
    if (selectedSeasonIndex != null
        && seasons != null
        && selectedSeasonIndex in seasons.indices)
        seasons[selectedSeasonIndex]
    else null

val TvShowEpisode.relativeProgress: Float?
    get() = progress?.let { (it.progressSeconds.toFloat() / duration.toFloat()).coerceIn(0f, 1f) }

val StreamsUiState.selectedIndex: Int?
    get() = selectedStreamId?.let { streamId ->
        streams.indexOfFirst { it.ident == streamId }
    }

@Composable
@ReadOnlyComposable
fun MediaDetailTvShow.generalInfoText(): String {
    val infos = mutableListOf<String>()
    years?.let { infos.add(it) }
    infos.add(stringResourceSafe(id = R.string.wsp_tv_show_count_of_seasons, numSeasons))
    if(genre.isNotEmpty()) {
        infos.add(genre.joinToString(separator = " / "))
    }
    return infos.joinToString(separator = MEDIA_INFO_SEPARATOR)
}

@Composable
@ReadOnlyComposable
fun TvShowSeason.requireName(): String =
    name ?: stringResourceSafe(id = R.string.wsp_tv_show_season_number, seasonNumber)

@Composable
@ReadOnlyComposable
fun TvShowMediaDetailUiState.selectedSeasonName(): String? = selectedSeason()?.requireName()


