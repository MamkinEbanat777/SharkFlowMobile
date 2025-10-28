package com.example.sharkflow.presentation.screens.marketing

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
import com.example.sharkflow.core.common.Lang

@Composable
fun FeaturesScreen() {
    val scrollState = rememberScrollState()
    val featureTitles = Lang.stringArray(R.array.features_array_title)
    val featureDescriptions = Lang.stringArray(R.array.features_array_desc)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Text(
            text = Lang.string(R.string.features_title),
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            color = colorScheme.primary,
        )

        Text(
            text = Lang.string(R.string.features_desc),
            textAlign = TextAlign.Center
        )

        featureTitles.forEachIndexed { index, title ->
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = featureDescriptions.getOrNull(index) ?: ""
                )
            }
        }
    }
}
