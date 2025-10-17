package com.example.sharkflow.ui.screens.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sharkflow.R
import com.example.sharkflow.data.local.language.Lang
import com.example.sharkflow.ui.screens.common.components.ContactForm

@Composable
fun SupportScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Lang.string(R.string.support_title),
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            color = colorScheme.primary
        )

        Text(
            text = Lang.string(R.string.support_subtitle),
            textAlign = TextAlign.Center
        )

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = Lang.string(R.string.support_desc),
                textAlign = TextAlign.Center
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(Lang.string(R.string.support_email))
                Text(Lang.string(R.string.support_telegram))
                Text(Lang.string(R.string.support_discord))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = Lang.string(R.string.support_form_prompt),
                textAlign = TextAlign.Center
            )

            ContactForm()
        }
    }
}
