package com.example.sharkflow.data.api.dto.task

import com.google.gson.annotations.SerializedName

data class TaskResponseDto(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("dueDate") val dueDate: String?,
    @SerializedName("priority") val priority: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
)

data class TasksListResponseDto(
    @SerializedName("tasks") val tasks: List<TaskResponseDto>
)

data class CreateTaskRequestDto(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("dueDate") val dueDate: String? = null,
    @SerializedName("status") val status: Status = Status.PENDING,
    @SerializedName("priority") val priority: Priority = Priority.MEDIUM,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

data class CreateTaskResponseDto(
    @SerializedName("message") val message: String,
    @SerializedName("task") val task: TaskResponseDto,
    @SerializedName("taskCount") val taskCount: Int
)

data class UpdateTaskRequestDto(
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("dueDate") val dueDate: String? = null,
    @SerializedName("status") val status: Status? = null,
    @SerializedName("priority") val priority: Priority? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

data class UpdateTaskResponseDto(
    @SerializedName("message") val message: String,
    @SerializedName("updated") val updated: UpdateTaskRequestDto
)

data class DeletedTaskInfoDto(
    @SerializedName("title") val title: String,
    @SerializedName("removedFromBoard") val removedFromBoard: Boolean = true
)

data class DeleteTaskResponseDto(
    @SerializedName("message") val message: String,
    @SerializedName("deletedTask") val deletedTask: DeletedTaskInfoDto
)

enum class Status { PENDING, IN_PROGRESS, COMPLETED, CANCELLED }
enum class Priority { LOW, MEDIUM, HIGH }

fun Status.displayName(): String = when (this) {
    Status.PENDING -> "В ожидании"
    Status.IN_PROGRESS -> "В процессе"
    Status.CANCELLED -> "Отменено"
    Status.COMPLETED -> "Выполнено"
}

fun Priority.displayName(): String = when (this) {
    Priority.LOW -> "Низкий"
    Priority.MEDIUM -> "Средний"
    Priority.HIGH -> "Высокий"
}
