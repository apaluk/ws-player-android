package com.apaluk.wsplayer.domain.use_case.media

import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.core.util.convertNonSuccess
import com.apaluk.wsplayer.domain.model.media.util.toMediaProgress
import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import com.apaluk.wsplayer.domain.repository.WatchHistoryRepository
import com.apaluk.wsplayer.ui.common.util.toUiState
import com.apaluk.wsplayer.ui.media_detail.MediaDetailUiState
import com.apaluk.wsplayer.ui.media_detail.MovieMediaDetailUiState
import com.apaluk.wsplayer.ui.media_detail.TvShowMediaDetailUiState
import com.apaluk.wsplayer.ui.media_detail.util.toMediaDetailUiState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetMediaDetailUiStateUseCase @Inject constructor(
    private val getMediaDetailsUseCase: GetMediaDetailsUseCase,
    private val streamCinemaRepository: StreamCinemaRepository,
    private val watchHistoryRepository: WatchHistoryRepository,
    private val getSelectedSeasonUseCase: GetSelectedSeasonUseCase,
    private val getSeasonEpisodesUseCase: GetSeasonEpisodesUseCase
) {
    operator fun invoke(mediaId: String): Flow<Resource<MediaDetailUiState>> = flow {
        emit(Resource.Loading())
        val mediaDetailResource = getMediaDetailsUseCase(mediaId)
        if (mediaDetailResource !is Resource.Success) {
            emit(mediaDetailResource.convertNonSuccess())
        } else if (mediaDetailResource.data == null) {
            emit(Resource.Error())
        } else {
            val mediaDetailUiState = mediaDetailResource.data.toMediaDetailUiState()
            emit(Resource.Success(mediaDetailUiState))
            when(mediaDetailUiState) {
                is MovieMediaDetailUiState -> {
                    // collect movie progress and keep updating it
                    emitAll(watchHistoryRepository.getMediaWatchHistory(mediaId)
                        .map {
                            Resource.Success(
                                mediaDetailUiState.copy(
                                    movie = mediaDetailUiState.movie.copy(
                                        progress = it.firstOrNull()?.toMediaProgress()
                                    )
                                )
                            )
                        }
                    )
                }
                is TvShowMediaDetailUiState -> {
                    val seasons = streamCinemaRepository.getTvShowSeasons(mediaId).last()
                    if(seasons !is Resource.Success) {
                        emit(seasons.convertNonSuccess())
                    } else if(seasons.data == null) {
                        emit(Resource.Error())
                    } else {
                        val selectedSeasonIndex = getSelectedSeasonUseCase(mediaId, seasons.data)
                        val selectedSeason = seasons.data.getOrNull(selectedSeasonIndex) ?: return@flow
                        val seasonEpisodes = getSeasonEpisodesUseCase(mediaId, selectedSeason.id).first()
                        emit(
                            Resource.Success(
                                mediaDetailUiState.copy(
                                    seasons = seasons.data,
                                    selectedSeasonIndex = selectedSeasonIndex,
                                    episodesUiState = seasonEpisodes.toUiState(),
                                    episodes = seasonEpisodes.data?.episodes,
                                    selectedEpisodeIndex = seasonEpisodes.data?.selectedEpisodeIndex
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}