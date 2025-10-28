package com.example.sharkflow.presentation.screens.marketing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sharkflow.R
import com.example.sharkflow.core.common.Lang
import com.example.sharkflow.presentation.common.Accordion

@Composable
fun FAQScreen() {
    val scrollState = rememberScrollState()
    val questions = Lang.stringArray(R.array.faq_array_question)
    val answers = Lang.stringArray(R.array.faq_array_answer)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = Lang.string(R.string.faq_title),
            color = colorScheme.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium,
        )

        Text(
            text = Lang.string(R.string.faq_desc),
            textAlign = TextAlign.Center
        )

        for (i in questions.indices) {
            Accordion(
                title = questions[i],
                content = answers[i]
            )
        }
    }
}
