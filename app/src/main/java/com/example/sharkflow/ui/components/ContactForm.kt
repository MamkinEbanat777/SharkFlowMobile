package com.example.sharkflow.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.sharkflow.R
import com.example.sharkflow.data.local.language.Lang

@Composable
fun ContactForm() {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var question by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = colorScheme.primary, shape = RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(top = 8.dp, start = 4.dp, end = 4.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = Lang.string(R.string.contact_form_title),
                style = typography.headlineLarge,
                modifier = Modifier.padding(top = 20.dp)
            )
            AppField(
                value = email,
                onValueChange = { email = it },
                label = Lang.string(R.string.contact_form_email)
            )
            AppField(
                value = name,
                onValueChange = { name = it },
                label = Lang.string(R.string.contact_form_name)
            )
            AppField(
                value = question,
                onValueChange = { question = it },
                label = Lang.string(R.string.contact_form_question),
                singleLine = false
            )
            AppButton(
                onClick = { /* отправка запроса */ },
                variant = AppButtonVariant.Primary,
                text = Lang.string(R.string.contact_form_send),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}