package com.example.sharkflow.core.validators

import android.content.Context
import com.example.sharkflow.core.presentation.ToastManager
import com.example.sharkflow.domain.model.Task

object TaskValidator {
    fun validateTitle(
        title: String,
        context: Context,
        existingTasks: List<Task> = emptyList(),
        currentTaskUuid: String? = null
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

            existingTasks.any {
                it.title.equals(trimmed, ignoreCase = true) && it.uuid != currentTaskUuid
            } -> {
                ToastManager.warning(context, "Задача с таким названием уже существует")
                false
            }

            else -> true
        }
    }
}
