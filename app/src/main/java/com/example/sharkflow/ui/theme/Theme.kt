package com.example.sharkflow.ui.theme

import android.os.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*

val LightColorScheme = lightColorScheme(
    primary = Blue700,       // Акценты, кнопки
    secondary = Blue500,     // Вторичные кнопки и чекбоксы
    background = White,      // Фон приложения
    surface = Gray100,       // Карточки, панели, диалоги
    onPrimary = White,       // Текст/иконки на primary
    onSecondary = White,     // Текст/иконки на secondary
    onBackground = Black,    // Основной текст
    onSurface = Black,       // Текст на карточках и панелях
    error = ErrorRed,
)

val DarkColorScheme = darkColorScheme(
    primary = Blue700,       // Акценты, кнопки
    secondary = Blue500,     // Вторичные кнопки и чекбоксы
    background = Gray900,    // Фон приложения
    surface = Gray800,       // Карточки, панели, диалоги
    onPrimary = White,       // Текст/иконки на primary
    onSecondary = White,     // Текст/иконки на secondary
    onBackground = White,    // Основной текст
    onSurface = White,       // Текст на карточках и панелях
    error = ErrorRed,
)

@Composable
fun SharkFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val targetScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = targetScheme,
        typography = Typography,
        content = content
    )
}