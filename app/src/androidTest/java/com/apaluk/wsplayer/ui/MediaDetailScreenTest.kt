package com.apaluk.wsplayer.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.apaluk.wsplayer.domain.model.media.MediaDetail
import com.apaluk.wsplayer.ui.common.util.UiState
import com.apaluk.wsplayer.ui.media_detail.MediaDetailScreenContent
import com.apaluk.wsplayer.ui.media_detail.MediaDetailUiState
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.core.util.formatFileSize
import com.apaluk.wsplayer.domain.model.media.MediaStream
import com.apaluk.wsplayer.domain.model.media.Subtitles
import com.apaluk.wsplayer.domain.model.media.VideoDefinition
import org.junit.Rule
import org.junit.Test

class MediaDetailScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun mediaDetailsSet_movieDataDisplayed() {
        composeRule.setContent {
            WsPlayerTheme {
                MediaDetailScreenContent(
                    uiState = MediaDetailUiState(
                        uiState = UiState.Content,
                        mediaDetail = MediaDetail(
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
                            duration = 2 * 3600 + 3 * 60 + 31
                        )
                    )
                )
            }
        }
        composeRule
            .onNodeWithText("Pulp fiction")
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText("Quentin Tarantino")
            .assertCountEquals(2)
        composeRule
            .onNodeWithText("Bruce Willis, John Travolta, Samuel L. Jackson")
            .assertIsDisplayed()
        composeRule
            .onNode(hasText("Thriller / Comedy", substring = true))
            .assertIsDisplayed()
        composeRule
            .onNodeWithText("2:03:31")
            .assertIsDisplayed()
        composeRule
            .onNodeWithText(LoremIpsum(50).values.joinToString(" "))
            .assertIsDisplayed()
    }

    @Test
    fun streamsNotSet_streamsNotVisible() {
        composeRule.setContent {
            WsPlayerTheme {
                MediaDetailScreenContent(
                    uiState = MediaDetailUiState(
                        uiState = UiState.Content,
                        mediaDetail = MediaDetail(
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
                            duration = 2 * 3600 + 3 * 60 + 31
                        )
                    )
                )
            }
        }
        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.wsp_media_select_stream))
            .assertDoesNotExist()
    }

    @Test
    fun streamsSet_streamsVisible() {
        composeRule.setContent {
            WsPlayerTheme {
                MediaDetailScreenContent(
                    uiState = MediaDetailUiState(
                        uiState = UiState.Content,
                        streams = listOf(
                            MediaStream(
                                ident = "",
                                size = 12345689000L,
                                duration = 5420,
                                speed = 1204.5,
                                video = VideoDefinition.HD,
                                audios = listOf("CZ"),
                                subtitles = listOf(Subtitles("EN", true))
                            ),
                            MediaStream(
                                ident = "",
                                size = 34545689000L,
                                duration = 5420,
                                speed = 1204.5,
                                video = VideoDefinition.FHD,
                                audios = emptyList(),
                                subtitles = listOf(Subtitles("EN", true), Subtitles("SK", false))
                            )
                        )
                    ),
                )
            }
        }
        composeRule
            .onNodeWithText(12345689000L.formatFileSize())
            .assertIsDisplayed()
            .assertHasClickAction()
        composeRule
            .onNodeWithText(34545689000L.formatFileSize())
            .assertIsDisplayed()
        composeRule
            .onNodeWithText("HD")
            .assertIsDisplayed()
        composeRule
            .onNodeWithText("EN")
            .assertIsDisplayed()
        composeRule
            .onNodeWithText("EN,SK")
            .assertIsDisplayed()
        composeRule
            .onNodeWithText("CZ")
            .assertIsDisplayed()
    }
}