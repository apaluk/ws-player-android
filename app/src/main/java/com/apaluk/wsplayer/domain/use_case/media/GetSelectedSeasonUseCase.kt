package com.apaluk.wsplayer.domain.use_case.media

import com.apaluk.wsplayer.domain.model.media.TvShowSeason
import javax.inject.Inject

class GetSelectedSeasonUseCase @Inject constructor(
) {

    suspend operator fun invoke(mediaId: String, seasons: List<TvShowSeason>): Int {
        // select 1st regular season
        seasons.forEachIndexed { index, tvShowSeason ->
            if(tvShowSeason.seasonNumber == 1)
                return index
        }
        return 0
    }
}