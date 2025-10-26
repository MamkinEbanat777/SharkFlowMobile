package com.example.sharkflow.domain.repository

import com.example.sharkflow.data.api.dto.board.*
import com.example.sharkflow.domain.model.Board

interface BoardRepository {
    suspend fun getBoards(): Result<List<Board>>
    suspend fun createBoard(title: String, color: String): Result<Board>

    suspend fun updateBoard(
        boardUuid: String,
        update: UpdateBoardRequestDto
    ): Result<UpdateBoardRequestDto>

    suspend fun deleteBoard(boardUuid: String): Result<DeletedBoardInfoDto>
}
