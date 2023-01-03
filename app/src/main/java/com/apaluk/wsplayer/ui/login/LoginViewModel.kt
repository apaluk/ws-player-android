package com.apaluk.wsplayer.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apaluk.wsplayer.core.login.LoginManager
import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.domain.use_case.login.LoginAndGetTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


data class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val loggingIn: Boolean = false,
    val loggedIn: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginAndGetTokenUseCase: LoginAndGetTokenUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun updateUserName(userName: String) {
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
            _uiState.update { it.copy(loggingIn = true) }
            val loginResult = loginAndGetTokenUseCase(
                _uiState.value.userName,
                _uiState.value.password
            ).last()
            _uiState.update { it.copy(loggingIn = false) }
            Timber.d("xxx loginResult=$loginResult data=${loginResult.data}")

            when(loginResult) {
                is Resource.Error -> {
                    LoginManager.token = null
                }
                is Resource.Success -> {
                    LoginManager.token = loginResult.data
                    _uiState.update { it.copy(loggedIn = true) }
                }
                is Resource.Loading -> {}
            }

        }
    }

    fun onLoggedIn() {
        _uiState.update { it.copy(loggedIn = false) }
    }

}