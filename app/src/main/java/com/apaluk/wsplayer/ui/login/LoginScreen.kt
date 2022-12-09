package com.apaluk.wsplayer.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apaluk.wsplayer.ui.common.composable.EditWithHeader
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LoginScreenContent(
        modifier = modifier,
        uiState = uiState,
        onUpdateUsername = viewModel::updateUserName,
        onUpdatePassword = viewModel::updatePassword,
        onLogin = viewModel::login
    )
}

@Composable
private fun LoginScreenContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onUpdateUsername: (String) -> Unit = {},
    onUpdatePassword: (String) -> Unit = {},
    onLogin: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Column(
            modifier = modifier
                .widthIn(max = 400.dp)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            EditWithHeader(
                header = stringResource(id = com.apaluk.wsplayer.R.string.wsp_login_username),
                editText = uiState.userName,
                onTextChanged = onUpdateUsername
            )
            Spacer(modifier = modifier.height(16.dp))
            EditWithHeader(
                header = stringResource(id = com.apaluk.wsplayer.R.string.wsp_login_password),
                editText = uiState.password,
                onTextChanged = onUpdatePassword
            )
            Spacer(modifier = modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { onLogin() }) {
                    Text(text = stringResource(id = com.apaluk.wsplayer.R.string.wsp_login_positive_button))
                }
                if(uiState.loggingIn) {
                    Spacer(modifier = modifier.width(32.dp))
                    CircularProgressIndicator(
                        modifier = modifier.size(32.dp)
                    )
                }
            }
        }

    }
}

@Preview
@Composable
private fun BasicPreview() {
    WsPlayerTheme {
        LoginScreenContent(
            modifier = Modifier,
            uiState = LoginUiState(
                userName = "apaluk",
                password = "nbusr123",
                loggingIn = true
            )
        )
    }
}
