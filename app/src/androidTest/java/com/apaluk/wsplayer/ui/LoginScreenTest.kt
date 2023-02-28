package com.apaluk.wsplayer.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.ui.common.util.UiState
import com.apaluk.wsplayer.ui.login.LoginScreenAction
import com.apaluk.wsplayer.ui.login.LoginScreenContent
import com.apaluk.wsplayer.ui.login.LoginUiState
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loadingState_showsLoadingSpinner() {
        composeRule.activity.setContent {
            LoginScreenContent(
                uiState = LoginUiState(uiState = UiState.Loading)
            )
        }
        composeRule
            .onNodeWithTag("fullScreenLoader")
            .assertExists()
    }

    @Test
    fun errorState_errorIsDisplayed() {
        composeRule.activity.setContent {
            LoginScreenContent(
                uiState = LoginUiState(
                    uiState = UiState.Content,
                    errorMessage = "error"
                ),
            )
        }
        composeRule
            .onNodeWithText("error")
            .assertExists()
    }

    @Test
    fun userNameSet_textFieldIsFilled() {
        composeRule.activity.setContent {
            LoginScreenContent(
                uiState = LoginUiState(
                    uiState = UiState.Content,
                    userName = "user",
                ),
            )
        }
        composeRule
            .onAllNodesWithText("user")
            .assertCountEquals(1)
    }

    @Test
    fun sendForm_loginWasTriggered() {
        var loginTriggered = false
        composeRule.activity.setContent {
            LoginScreenContent(
                uiState = LoginUiState(
                    uiState = UiState.Content
                ),
                onLoginScreenAction = { action ->
                    assertThat(action).isEqualTo(LoginScreenAction.TriggerLogin)
                    loginTriggered = true
                }
            )
        }
        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.wsp_login_positive_button))
            .performClick()

        assertThat(loginTriggered).isTrue()
    }
}