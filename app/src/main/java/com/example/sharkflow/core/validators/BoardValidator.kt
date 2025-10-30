package com.example.sharkflow.core.validators

import android.content.Context
import com.example.sharkflow.core.presentation.ToastManager
import com.example.sharkflow.domain.model.Board

object BoardValidator {

    fun validateTitle(
        title: String,
        context: Context,
        existingBoards: List<Board> = emptyList(),
        excludeUuid: String? = null
    ): Boolean {
        val trimmed = title.trim()
        return when {
            trimmed.isEmpty() -> {
                ToastManager.warning(context, "Название не может быть пустым")
                false
            }

            trimmed.length > 64 -> {
                ToastManager.warning(context, "Название не может быть длиннее 64 символов")
                false
            }

            existingBoards.any {
                it.title.equals(
                    trimmed,
                    ignoreCase = true
                ) && it.uuid != excludeUuid
            } -> {
                ToastManager.warning(context, "Доска с таким названием уже существует")
                false
            }

            else -> true
        }
    }
}
