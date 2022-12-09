package com.apaluk.wsplayer.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.domain.use_case.login.LoginAndGetTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


data class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val loggingIn: Boolean = false,
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
            Timber.d("xxx loginResult=$loginResult")

//            when(loginResult) {
//                is Resource.Error -> {}
//                is Resource.Success -> {}
//                is Resource.Loading -> {}
//            }

        }
    }

}