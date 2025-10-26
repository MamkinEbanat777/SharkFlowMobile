package com.example.sharkflow.data.api

import com.example.sharkflow.data.api.dto.task.*
import retrofit2.Response
import retrofit2.http.*

interface TaskApi {
    @GET("boards/{boardUuid}/tasks")
    suspend fun getTasks(@Path("boardUuid") boardUuid: String): Response<TasksListResponseDto>

    @POST("boards/{boardUuid}/tasks")
    suspend fun createTask(
        @Path("boardUuid") boardUuid: String,
        @Body request: CreateTaskRequestDto
    ): Response<CreateTaskResponseDto>

    @PATCH("boards/{boardUuid}/tasks/{taskUuid}")
    suspend fun updateTask(
        @Path("boardUuid") boardUuid: String,
        @Path("taskUuid") taskUuid: String,
        @Body update: UpdateTaskRequestDto
    ): Response<UpdateTaskResponseDto>

    @DELETE("boards/{boardUuid}/tasks/{taskUuid}")
    suspend fun deleteTask(
        @Path("boardUuid") boardUuid: String,
        @Path("taskUuid") taskUuid: String
    ): Response<DeleteTaskResponseDto>
}
