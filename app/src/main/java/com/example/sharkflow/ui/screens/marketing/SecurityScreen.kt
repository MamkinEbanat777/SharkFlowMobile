package com.example.sharkflow.ui.screens.marketing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sharkflow.R
import com.example.sharkflow.utils.Lang

@Composable
fun SecurityScreen() {
    val scrollState = rememberScrollState()

    val titles = Lang.stringArray(R.array.security_array_title)
    val descriptions = Lang.stringArray(R.array.security_array_desc)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = Lang.string(R.string.security_title),
            style = MaterialTheme.typography.displayMedium,
            color = colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Text(
            text = Lang.string(R.string.security_subtitle),
            textAlign = TextAlign.Center
        )
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            titles.zip(descriptions).forEach { (title, desc) ->
                SecurityItem(title = title, description = desc)
            }
        }
    }
}

@Composable
private fun SecurityItem(title: String, description: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            color = colorScheme.primary
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start
        )
    }
}