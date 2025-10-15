package com.example.sharkflow.ui.screens.marketing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sharkflow.R
import com.example.sharkflow.data.local.language.Lang

@Composable
fun AboutScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        val team = listOf(
            Triple(
                Lang.string(R.string.about_member_andrey_name),
                Lang.string(R.string.about_member_andrey_role),
                R.drawable.dev1
            ),
            Triple(
                Lang.string(R.string.about_member_katerina_name),
                Lang.string(R.string.about_member_katerina_role),
                R.drawable.dev2
            ),
            Triple(
                Lang.string(R.string.about_member_igor_name),
                Lang.string(R.string.about_member_igor_role),
                R.drawable.dev3
            ),
            Triple(
                Lang.string(R.string.about_member_olga_name),
                Lang.string(R.string.about_member_olga_role),
                R.drawable.dev4
            )
        )

        val descriptions = listOf(
            Lang.string(R.string.about_member_andrey_desc),
            Lang.string(R.string.about_member_katerina_desc),
            Lang.string(R.string.about_member_igor_desc),
            Lang.string(R.string.about_member_olga_desc)
        )

        Text(
            text = Lang.string(R.string.about_title),
            color = colorScheme.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium,
        )

        Text(
            text = Lang.string(R.string.about_app_desc),
            textAlign = TextAlign.Center
        )

        team.forEachIndexed { index, member ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorScheme.primary, shape = RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Image(
                        painter = painterResource(id = member.third),
                        contentDescription = member.first,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = member.first,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onPrimary,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                    Text(
                        text = member.second,
                        textAlign = TextAlign.Center,
                        color = colorScheme.onPrimary,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                    Text(
                        text = descriptions[index],
                        textAlign = TextAlign.Center,
                        color = colorScheme.onPrimary,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
            }
        }
    }
}
