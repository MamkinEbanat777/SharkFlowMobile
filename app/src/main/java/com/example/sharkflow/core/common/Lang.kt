package com.example.sharkflow.core.common

import android.content.Context
import androidx.annotation.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.sharkflow.domain.repository.LanguageRepository
import java.util.Locale

object Lang {
    private lateinit var languageRepository: LanguageRepository

    fun init(repository: LanguageRepository) {
        languageRepository = repository
    }

    @Composable
    fun string(@StringRes resId: Int): String {
        check(::languageRepository.isInitialized) { "Lang not initialized" }

        val context = LocalContext.current
        val currentLanguage by languageRepository.currentLanguageFlow.collectAsState()
        val localeContext =
            remember(currentLanguage) { context.createLocaleContext(currentLanguage) }
        return localeContext.getString(resId)
    }

    @Composable
    fun stringArray(@ArrayRes resId: Int): Array<String> {
        check(::languageRepository.isInitialized) { "Lang not initialized" }

        val context = LocalContext.current
        val currentLanguage by languageRepository.currentLanguageFlow.collectAsState()
        val localeContext =
            remember(currentLanguage) { context.createLocaleContext(currentLanguage) }
        return localeContext.resources.getStringArray(resId)
    }

    private fun Context.createLocaleContext(language: String): Context {
        val locale = Locale.Builder().setLanguage(language).build()
        val config = resources.configuration
        config.setLocale(locale)
        return createConfigurationContext(config)
    }
}
