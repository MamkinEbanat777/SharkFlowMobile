package com.example.sharkflow.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

val LightColorScheme = lightColorScheme(
    primary = ColorPrimary,       // Акценты, кнопки
    secondary = ColorSecondary,     // Вторичные кнопки и чекбоксы
    background = ColorBackgroundLight,      // Фон приложения
    surface = ColorSurfaceLight,       // Карточки, панели, диалоги
    onPrimary = ColorBackgroundLight,       // Текст/иконки на primary
    onSecondary = ColorBackgroundLight,     // Текст/иконки на secondary
    onBackground = ColorTextPrimary,    // Основной текст
    onSurface = ColorTextPrimary,       // Текст на карточках и панелях
    error = ColorError,
)

val DarkColorScheme = darkColorScheme(
    primary = ColorPrimary,       // Акценты, кнопки
    secondary = ColorSecondary,     // Вторичные кнопки и чекбоксы
    background = ColorBackgroundDark,    // Фон приложения
    surface = ColorSurfaceDark,       // Карточки, панели, диалоги
    onPrimary = ColorBackgroundLight,       // Текст/иконки на primary
    onSecondary = ColorBackgroundLight,     // Текст/иконки на secondary
    onBackground = ColorBackgroundLight,    // Основной текст
    onSurface = ColorBackgroundLight,       // Текст на карточках и панелях
    error = ColorError,
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