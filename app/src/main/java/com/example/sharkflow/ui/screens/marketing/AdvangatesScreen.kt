package com.example.sharkflow.ui.screens.marketing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.sharkflow.R
import com.example.sharkflow.data.local.language.Lang
import com.example.sharkflow.ui.components.Link

@Composable
fun AdvantagesScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = Lang.string(R.string.advantages_title),
            color = colorScheme.primary,
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = Lang.string(R.string.advantages_desc),
            textAlign = TextAlign.Center
        )

        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            AdvantageItem(
                icon = Icons.Filled.MoneyOff,
                title = R.string.advantages_free_title,
                description = R.string.advantages_free_desc
            )
            AdvantageItem(
                icon = Icons.Filled.OpenWith,
                title = R.string.advantages_open_source_title,
                description = R.string.advantages_open_source_desc,
                linkText = R.string.advantages_open_source_link,
                linkUrl = "https://github.com/MamkinEbanat777/sharkflow"
            )
            AdvantageItem(
                icon = Icons.Filled.AdsClick,
                title = R.string.advantages_simplicity_title,
                description = R.string.advantages_simplicity_desc
            )
            AdvantageItem(
                icon = Icons.Filled.Security,
                title = R.string.advantages_security_title,
                description = R.string.advantages_security_desc
            )
            AdvantageItem(
                icon = Icons.Filled.FileDownloadOff,
                title = R.string.advantages_no_install_title,
                description = R.string.advantages_no_install_desc
            )
        }
    }
}


@Composable
private fun AdvantageItem(
    icon: ImageVector,
    title: Int,
    description: Int,
    linkText: Int? = null,
    linkUrl: String? = null
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = Lang.string(title),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = Lang.string(title),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }

    if (linkText != null && linkUrl != null) {
        Link(
            fullText = Lang.string(description),
            linkText = Lang.string(linkText),
            url = linkUrl
        )
    } else {
        Text(Lang.string(description))
    }
}