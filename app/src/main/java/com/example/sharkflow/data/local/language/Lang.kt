package com.example.sharkflow.data.local.language

import android.content.Context
import androidx.annotation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

object Lang {
    @Composable
    fun string(@StringRes resId: Int): String {
        val context = LocalContext.current
        val localeContext = context.createLocaleContext(LanguageState.currentLanguage)
        return localeContext.getString(resId)
    }

    @Composable
    fun stringArray(@ArrayRes resId: Int): Array<String> {
        val context = LocalContext.current
        val localeContext = context.createLocaleContext(LanguageState.currentLanguage)
        return localeContext.resources.getStringArray(resId)
    }

    private fun Context.createLocaleContext(language: String): Context {
        val locale = Locale.Builder().setLanguage(language).build()
        val config = resources.configuration
        config.setLocale(locale)
        return createConfigurationContext(config)
    }
}