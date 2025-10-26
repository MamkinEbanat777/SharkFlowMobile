package com.example.sharkflow.presentation.navigation.components

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.core.graphics.createBitmap
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.sharkflow.presentation.navigation.viewmodel.LanguageViewModel
import com.murgupluoglu.flagkit.FlagKit
import java.util.Locale

@Composable
fun LanguageButton(languageViewModel: LanguageViewModel = hiltViewModel()) {
    var expanded by remember { mutableStateOf(false) }

    val deviceLang = Locale.getDefault().language

    val selectedLanguageFromVm by languageViewModel.currentLanguageFlow.collectAsState()

    var displayedLanguage by remember { mutableStateOf(selectedLanguageFromVm) }

    LaunchedEffect(selectedLanguageFromVm) {
        displayedLanguage = selectedLanguageFromVm
    }

    val allLanguages = remember {
        listOf(
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
    }

    val languages = remember(deviceLang) {
        allLanguages.sortedWith(compareByDescending { it.first == deviceLang })
    }

    val currentLang = remember(displayedLanguage, languages) {
        languages.firstOrNull { it.first == displayedLanguage } ?: languages.first()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { expanded = true }
            .padding(4.dp)
    ) {
        FlagIcon(code = currentLang.third, labelWithEmoji = currentLang.second, sizeDp = 24.dp)
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        languages.forEach { (langCode, label, countryCode) ->
            val textWithoutEmoji = label.substringBeforeLast(' ')
            DropdownMenuItem(
                text = { Text(textWithoutEmoji) },
                onClick = {
                    displayedLanguage = langCode
                    languageViewModel.updateLanguage(langCode)
                    expanded = false
                },
                leadingIcon = {
                    FlagIcon(code = countryCode, labelWithEmoji = label, sizeDp = 20.dp)
                }
            )
        }
    }
}

private fun drawableToImageBitmap(drawable: Drawable, width: Int, height: Int): ImageBitmap {
    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, width, height)
    drawable.draw(canvas)
    return bitmap.asImageBitmap()
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

    if (flagResId != 0) {
        val painter = painterResource(id = flagResId)
        Image(
            painter = painter,
            contentDescription = "$code flag",
            modifier = Modifier.size(sizeDp)
        )
        return
    }


    val flagDrawable = remember(code) { FlagKit.getDrawable(context, code) }

    if (flagDrawable != null) {
        val painter = remember(flagDrawable, sizeDp, density) {
            val sizePx = with(density) { sizeDp.toPx().toInt().coerceAtLeast(1) }
            val imageBitmap = drawableToImageBitmap(flagDrawable, sizePx, sizePx)
            BitmapPainter(imageBitmap)
        }

        Image(
            painter = painter,
            contentDescription = "$code flag",
            modifier = Modifier.size(sizeDp)
        )
        return
    }

    val emoji = labelWithEmoji.substringAfterLast(' ', "")
    if (emoji.isNotBlank()) {
        Text(text = emoji, fontSize = 16.sp, modifier = Modifier.size(sizeDp))
    } else {
        Spacer(modifier = Modifier.size(sizeDp))
    }
}
