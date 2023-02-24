package com.apaluk.wsplayer.ui.media_detail.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.core.util.Constants
import com.apaluk.wsplayer.domain.model.media.MediaDetail
import com.apaluk.wsplayer.domain.model.media.MediaDetailMovie
import com.apaluk.wsplayer.domain.model.media.MediaDetailTvShow
import com.apaluk.wsplayer.domain.model.media.TvShowSeason
import com.apaluk.wsplayer.ui.common.util.stringResourceSafe
import com.apaluk.wsplayer.ui.media_detail.MediaDetailUiState
import com.apaluk.wsplayer.ui.media_detail.MovieMediaDetailUiState
import com.apaluk.wsplayer.ui.media_detail.TvShowMediaDetailUiState

private const val MEDIA_INFO_SEPARATOR = "  ${Constants.CHAR_BULLET}  "

fun MediaDetail.toMediaDetailUiState(): MediaDetailUiState =
    when(this) {
        is MediaDetailMovie -> MovieMediaDetailUiState(movie = this)
        is MediaDetailTvShow -> TvShowMediaDetailUiState(tvShow = this)
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
fun TvShowMediaDetailUiState.selectedSeasonName(): String? {
    return if (selectedSeasonIndex != null && seasons != null && selectedSeasonIndex in seasons.indices)
        seasons[selectedSeasonIndex].requireName()
    else null
}

