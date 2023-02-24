package com.apaluk.wsplayer.domain.use_case.media

import javax.inject.Inject

class GetSelectedEpisodeUseCase @Inject constructor(

) {
    suspend operator fun invoke(mediaId: String, seasonId: String): Int {
        return 0
    }
}