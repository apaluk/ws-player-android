package com.apaluk.wsplayer.core.testing

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.domain.model.media.*
import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class StreamCinemaRepositoryFake: StreamCinemaRepository {

    private val searchResults = ('a'..'z').map {  c ->
        SearchResultItem(
            id = "",
            title = "a$c",
            originalTitle = null,
            year = "",
            genre = emptyList(),
            duration = 1,
            cast = emptyList(),
            director = emptyList(),
            imageUrl = null
        )
    }.toList()

    private val mediaStreams = listOf(
        MediaStream(
            ident = "",
            size = 123456,
            duration = 125,
            speed = 123456.0 / 125.0,
            video = VideoDefinition.SD,
            audios = emptyList(),
            subtitles = listOf(Subtitles("sk", false))
        ),
        MediaStream(
            ident = "",
            size = 234567,
            duration = 125,
            speed = 234567.0 / 125.0,
            video = VideoDefinition.HD,
            audios = listOf("en", "cs"),
            subtitles = listOf(Subtitles("sk", false))
        ),
        MediaStream(
            ident = "",
            size = 1234567,
            duration = 125,
            speed = 1234567.0 / 125.0,
            video = VideoDefinition.UHD_4K,
            audios = emptyList(),
            subtitles = emptyList()
        ),
    )

    override fun getMediaStreams(mediaId: String): Flow<Resource<List<MediaStream>>> = flowOf(
        Resource.Loading(),
        Resource.Success(data = mediaStreams)
    )

    override fun search(text: String): Flow<Resource<List<SearchResultItem>>> = flowOf(
        Resource.Loading(),
        Resource.Success(data = searchResults
            .filter { it.title.contains(text) }
        )
    )


    override fun getMediaDetails(mediaId: String): Flow<Resource<MediaDetail>> = flowOf(
        Resource.Loading(),
        Resource.Success(
            data = MediaDetail(
                id = "",
                title = "Pulp fiction",
                originalTitle = "Pulp fiction",
                year = "1994",
                directors = listOf("Quentin Tarantino"),
                writer = listOf("Quentin Tarantino"),
                cast = listOf("Bruce Willis", "John Travolta", "Samuel L. Jackson"),
                genre = listOf("Thriller", "Comedy"),
                plot = LoremIpsum(50).values.joinToString(" "),
                imageUrl = null,
                duration = 7444
            )
        )
    )

    override fun getTvShowSeasons(mediaId: String): Flow<Resource<List<TvShowSeason>>> {
        return emptyFlow()  // TODO
    }

    override fun getTvShowSeasonEpisodes(mediaId: String): Flow<Resource<List<TvShowEpisode>>> {
        return emptyFlow()  // TODO
    }
}