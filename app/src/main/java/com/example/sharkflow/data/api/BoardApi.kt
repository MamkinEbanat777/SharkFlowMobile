package com.example.sharkflow.data.api

import com.example.sharkflow.data.api.dto.board.*
import retrofit2.Response
import retrofit2.http.*

interface BoardApi {
    @GET("boards")
    suspend fun getBoards(): Response<BoardsListResponseDto>

    @POST("boards")
    suspend fun createBoard(@Body board: CreateBoardRequestDto): Response<CreateBoardResponseDto>

    @PATCH("boards/{boardUuid}")
    suspend fun updateBoard(
        @Path("boardUuid") boardUuid: String,
        @Body board: UpdateBoardRequestDto
    ): Response<UpdateBoardResponseDto>

    @DELETE("boards/{boardUuid}")
    suspend fun deleteBoard(@Path("boardUuid") boardUuid: String): Response<DeleteBoardResponseDto>
}
