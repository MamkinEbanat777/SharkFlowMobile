package com.example.sharkflow.domain.model

enum class TaskStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    fun displayName(): String = when (this) {
        PENDING -> "В ожидании"
        IN_PROGRESS -> "В процессе"
        CANCELLED -> "Отменено"
        COMPLETED -> "Выполнено"
    }
}
