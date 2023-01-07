package com.apaluk.wsplayer.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apaluk.wsplayer.core.login.LoginManager
import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.ui.common.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


data class LoginUiState(
    val uiState: UiState = UiState.Loading,
    val userName: String = "",
    val password: String = "",
    val loggingIn: Boolean = false,
    val loggedIn: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginManager: LoginManager
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow().apply {
        viewModelScope.launch {
            loginManager.loginState.collect { loginState ->
                _uiState.update {
                    it.copy(
                        uiState = if (loginState.shouldShowLoading()) UiState.Loading else UiState.Content,
                        loggedIn = loginState == LoginManager.LoginState.LoggedIn
                    )
                }
            }
        }
    }

    fun updateUserName(userName: String) {
        _uiState.value
        _uiState.update {
            it.copy(userName = userName)
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(loggingIn = true, errorMessage = null) }
            val loginResult = loginManager.tryLogin(
                username = _uiState.value.userName,
                password = _uiState.value.password
            )
            _uiState.update { it.copy(
                loggingIn = false,
                loggedIn = loginResult is Resource.Success,
                errorMessage = loginResult.message
            ) }
        }
    }

    fun onLoggedIn() {
        _uiState.update { it.copy(loggedIn = false) }
    }

}

private fun LoginManager.LoginState.shouldShowLoading() =
    this == LoginManager.LoginState.Initializing || this == LoginManager.LoginState.LoggedIn