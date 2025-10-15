package com.example.sharkflow.ui.screens.marketing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.sharkflow.R
import com.example.sharkflow.data.local.language.Lang

@Composable
fun HowItWorksScreen() {
    val scrollState = rememberScrollState()

    val numbers = Lang.stringArray(R.array.how_it_works_array_step_number)
    val titles = Lang.stringArray(R.array.how_it_works_array_step_title)
    val descriptions = Lang.stringArray(R.array.how_it_works_array_step_desc)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Text(
            text = Lang.string(R.string.how_it_works_title),
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            color = colorScheme.primary,
        )

        Text(
            text = Lang.string(R.string.how_it_works_subtitle),
            textAlign = TextAlign.Center
        )

        for (i in numbers.indices) {
            HowItWorksStep(
                number = numbers[i],
                title = titles[i],
                description = descriptions[i]
            )
        }
    }
}

@Composable
private fun HowItWorksStep(number: String, title: String, description: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            number,
            color = colorScheme.onPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(colorScheme.primary, shape = RoundedCornerShape(30.dp))
                .padding(22.dp, 15.dp)
        )
        Text(
            title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        Text(description, textAlign = TextAlign.Center)
    }
}