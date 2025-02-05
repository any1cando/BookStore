package com.example.bookstoreapp.ui.login_screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bookstoreapp.ui.theme.ButtonColor


@Composable
fun RoundedCornerTextField(
    text: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = { onValueChange(it) },
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,  // Если мы не выбрали TextField
            focusedContainerColor = Color.White,  // Если мы выбрали TextField
            unfocusedIndicatorColor = Color.Transparent,  // Отвечает за цвет палочки снизу TextField
            focusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth().border(2.dp, ButtonColor, RoundedCornerShape(25.dp)),
        label = { Text(text = label, color = Color.Gray) },
        singleLine = true
    )

}