package com.apaluk.wsplayer.ui.common.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithHeader(
    modifier: Modifier = Modifier,
    header: String,
    editText: String,
    onTextChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column {
        Text(
            modifier = modifier
                .padding(8.dp),
            text = header,
            style = MaterialTheme.typography.bodySmall
        )
        TextField(
            modifier = modifier
                .fillMaxWidth(),
            value = editText,
            onValueChange = onTextChanged,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation
        )
    }
}

@Preview
@Composable
fun TextWithHeaderPreview() {
    WsPlayerTheme {
        TextFieldWithHeader(header = "Username", editText = "hello", onTextChanged = {})
    }
}