package com.apaluk.wsplayer.ui.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EditWithHeader(
    modifier: Modifier = Modifier,
    header: String,
    editText: String,
    onTextChanged: (String) -> Unit
) {
    Text(
        modifier = modifier
            .padding(vertical = 4.dp),
        text = header,
        style = MaterialTheme.typography.bodySmall
    )
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xDDDDDDFF))
            .padding(horizontal = 4.dp, vertical = 8.dp),
        value = editText,
        onValueChange = onTextChanged,

        )

}