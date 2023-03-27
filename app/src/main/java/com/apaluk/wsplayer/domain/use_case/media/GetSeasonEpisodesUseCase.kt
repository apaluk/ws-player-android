package com.apaluk.wsplayer.domain.use_case.media

import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.core.util.convertNonSuccess
import com.apaluk.wsplayer.domain.model.media.TvShowSeasonEpisodes
import com.apaluk.wsplayer.domain.model.media.util.toMediaProgress
import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import com.apaluk.wsplayer.domain.repository.WatchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSeasonEpisodesUseCase @Inject constructor(
    private val streamCinemaRepository: StreamCinemaRepository,
    private val watchHistoryRepository: WatchHistoryRepository,
    private val getSelectedEpisode: GetSelectedEpisodeUseCase
) {
    operator fun invoke(mediaId: String, season: String): Flow<Resource<TvShowSeasonEpisodes>> {
        return combine(
            streamCinemaRepository.getTvShowSeasonEpisodes(season),
            watchHistoryRepository.getSeasonEpisodesWatchHistory(mediaId, season)
        ) { episodesResource, history ->
            if (episodesResource !is Resource.Success) {
                episodesResource.convertNonSuccess()
            } else if(episodesResource.data == null) {
                Resource.Success(TvShowSeasonEpisodes())
            } else {
                val episodes = episodesResource.data.map { episode ->
                    val progress = history
                        .find { it.episodeId == episode.id }
                        ?.toMediaProgress()
                    episode.copy(
                        progress = progress
                    )
                }
                val selectedEpisode = getSelectedEpisode(mediaId, season, episodes)
                Resource.Success(TvShowSeasonEpisodes(episodes, selectedEpisode))
            }
        }
    }
}
