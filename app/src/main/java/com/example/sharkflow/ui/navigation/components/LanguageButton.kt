package com.example.sharkflow.ui.navigation.components

import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import com.example.sharkflow.data.local.language.*
import com.murgupluoglu.flagkit.FlagKit
import java.util.Locale

@Composable
fun LanguageButton() {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf(LanguageState.currentLanguage) }

    LaunchedEffect(LanguageState.currentLanguage) {
        selectedLanguage = LanguageState.currentLanguage
    }

    val allLanguages = listOf(
        Triple("ru", "Русский 🇷🇺", "ru"),
        Triple("en", "English 🇺🇸", "us"),
        Triple("de", "Deutsch 🇩🇪", "de"),
        Triple("fr", "Français 🇫🇷", "fr"),
        Triple("es", "Español 🇪🇸", "es"),
        Triple("ar", "العربية 🇸🇦", "sa"),
        Triple("pt", "Português 🇵🇹", "pt"),
        Triple("hi", "हिन्दी 🇮🇳", "in"),
        Triple("ja", "日本語 🇯🇵", "jp"),
        Triple("zh", "中文 🇨🇳", "cn")
    )

    val deviceLang = Locale.getDefault().language

    val languages = allLanguages.sortedWith(compareByDescending {
        it.first == deviceLang
    })

    val currentLang = languages.firstOrNull { it.first == selectedLanguage } ?: languages.first()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { expanded = true }
            .padding(4.dp)
    ) {
        FlagIcon(code = currentLang.third, labelWithEmoji = currentLang.second, sizeDp = 24.dp)
        //Spacer(modifier = Modifier.width(8.dp))
        //Text(text = currentLang.first.uppercase(), fontSize = 16.sp)
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        languages.forEach { (langCode, label, countryCode) ->
            val textWithoutEmoji = label.substringBeforeLast(' ')
            DropdownMenuItem(
                text = { Text(textWithoutEmoji) },
                onClick = {
                    LanguagePreference.set(context, langCode)
                    LanguageState.currentLanguage = langCode
                    selectedLanguage = langCode
                    expanded = false
                },
                leadingIcon = {
                    FlagIcon(code = countryCode, labelWithEmoji = label, sizeDp = 20.dp)
                }
            )
        }
    }
}

@Composable
private fun FlagIcon(
    code: String,
    labelWithEmoji: String,
    sizeDp: Dp = 24.dp
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val flagResId = remember(code) { FlagKit.getResId(context, code) }
    val flagDrawable = remember(code) { FlagKit.getDrawable(context, code) }

    when {
        flagResId != 0 -> {
            Image(
                painter = painterResource(id = flagResId),
                contentDescription = "$code flag",
                modifier = Modifier.size(sizeDp)
            )
        }

        flagDrawable != null -> {
            AndroidView(
                factory = { ctx ->
                    ImageView(ctx).apply {
                        setImageDrawable(flagDrawable)
                        val sizePx = with(density) { sizeDp.toPx().toInt() }
                        layoutParams = ViewGroup.LayoutParams(sizePx, sizePx)
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                },
                modifier = Modifier.size(sizeDp)
            )
        }

        else -> {
            val emoji = labelWithEmoji.substringAfterLast(' ', "")
            if (emoji.isNotBlank()) {
                Text(text = emoji, fontSize = 16.sp, modifier = Modifier.size(sizeDp))
            } else {
                Spacer(modifier = Modifier.size(sizeDp))
            }
        }
    }
}
