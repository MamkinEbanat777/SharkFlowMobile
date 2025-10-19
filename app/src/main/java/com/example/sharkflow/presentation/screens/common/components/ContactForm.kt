package com.example.sharkflow.presentation.screens.common.components

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
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.utils.Lang

@Composable
fun ContactForm() {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var question by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var questionError by remember { mutableStateOf(false) }

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
                onValueChange = {
                    email = it
                    if (emailError) emailError = false
                },
                label = Lang.string(R.string.contact_form_email),
                isError = emailError
            )

            AppField(
                value = name,
                onValueChange = {
                    name = it
                    if (nameError) nameError = false
                },
                label = Lang.string(R.string.contact_form_name),
                isError = nameError
            )

            AppField(
                value = question,
                onValueChange = {
                    question = it
                    if (questionError) questionError = false
                },
                label = Lang.string(R.string.contact_form_question),
                singleLine = false,
                isError = questionError
            )

            AppButton(
                onClick = {
                    var hasError = false
                    if (email.isEmpty()) {
                        emailError = true; hasError = true
                    }
                    if (name.isEmpty()) {
                        nameError = true; hasError = true
                    }
                    if (question.isEmpty()) {
                        questionError = true; hasError = true
                    }

                    if (!hasError) {
                        // типо запрос
                    }
                },
                variant = AppButtonVariant.Primary,
                text = Lang.string(R.string.contact_form_send),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}