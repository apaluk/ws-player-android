package com.apaluk.wsplayer.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apaluk.wsplayer.R
import com.apaluk.wsplayer.ui.common.composable.TextFieldWithHeader
import com.apaluk.wsplayer.ui.common.composable.UiStateAnimator
import com.apaluk.wsplayer.ui.common.util.stringResourceSafe
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onSuccessFullLogin: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    UiStateAnimator(uiState = uiState.uiState) {
        LoginScreenContent(
            modifier = modifier,
            uiState = uiState,
            onUpdateUsername = viewModel::updateUserName,
            onUpdatePassword = viewModel::updatePassword,
            onLogin = viewModel::login
        )
    }
    LaunchedEffect(uiState.loggedIn) {
        if(uiState.loggedIn) {
            viewModel.onLoggedIn()
            onSuccessFullLogin()
        }
    }
}

@Composable
private fun LoginScreenContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onUpdateUsername: (String) -> Unit = {},
    onUpdatePassword: (String) -> Unit = {},
    onLogin: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            modifier = modifier
                .widthIn(max = 400.dp)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 32.dp),
                text = stringResourceSafe(id = R.string.wsp_login_welcome),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 32.dp),
                text = stringResourceSafe(id = R.string.wsp_login_instructions),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            TextFieldWithHeader(
                header = stringResourceSafe(id = com.apaluk.wsplayer.R.string.wsp_login_username),
                editText = uiState.userName,
                onTextChanged = onUpdateUsername,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = modifier.height(16.dp))
            TextFieldWithHeader(
                header = stringResourceSafe(id = com.apaluk.wsplayer.R.string.wsp_login_password),
                editText = uiState.password,
                onTextChanged = onUpdatePassword,
                modifier = modifier,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { onLogin() }) {
                    Text(text = stringResourceSafe(id = com.apaluk.wsplayer.R.string.wsp_login_positive_button))
                }
                if(uiState.loggingIn) {
                    Spacer(modifier = modifier.width(32.dp))
                    CircularProgressIndicator(
                        modifier = modifier.size(32.dp)
                    )
                }
            }

            uiState.errorMessage?.let { errorMessage ->
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = TextStyle(
                            fontSize = 12.sp
                        )
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
                loggingIn = true,
                errorMessage = "Something went wrong!"
            )
        )
    }
}
