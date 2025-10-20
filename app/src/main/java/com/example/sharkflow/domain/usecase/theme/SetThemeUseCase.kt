package com.example.sharkflow.domain.usecase.theme

import com.example.sharkflow.domain.repository.ThemeRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    operator fun invoke(isDark: Boolean) {
        themeRepository.setDarkTheme(isDark)
    }
}