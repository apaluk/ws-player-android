package com.apaluk.wsplayer.core.login

import com.apaluk.wsplayer.core.util.Resource
import kotlinx.coroutines.flow.StateFlow

interface LoginManager {

    enum class LoginState {
        Initializing, LoggingIn, LoggedIn, LoggedOut
    }

    val loginState: StateFlow<LoginState>
    val loginError: StateFlow<String?>

    suspend fun tryLogin(username: String, password: String): Resource<Unit>
    suspend fun logout()
    suspend fun getWebShareToken(): String
}