package com.example.sharkflow.domain.model

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH;

    fun displayName(): String = when (this) {
        LOW -> "Низкий"
        MEDIUM -> "Средний"
        HIGH -> "Высокий"
    }
}
