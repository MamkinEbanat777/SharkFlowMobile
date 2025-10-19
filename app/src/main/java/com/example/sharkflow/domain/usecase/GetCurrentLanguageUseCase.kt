package com.example.sharkflow.domain.usecase

import com.example.sharkflow.domain.repository.LanguageRepository
import jakarta.inject.Inject

class GetCurrentLanguageUseCase @Inject constructor(
    private val repository: LanguageRepository
) {
    suspend operator fun invoke(): String {
        return repository.getCurrentLanguage()
    }
}