package com.example.sharkflow.domain.usecase.theme

import com.example.sharkflow.domain.repository.ThemeRepository
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    suspend operator fun invoke(): Boolean = themeRepository.isDarkTheme()
}