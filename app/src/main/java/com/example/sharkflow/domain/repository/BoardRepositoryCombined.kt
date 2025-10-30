package com.example.sharkflow.domain.repository

import com.example.sharkflow.data.api.dto.board.*
import com.example.sharkflow.domain.model.Board
import kotlinx.coroutines.flow.Flow

interface BoardRepositoryCombined {
    fun getBoardsFlow(): Flow<List<Board>>
    suspend fun refreshBoards()
    suspend fun createBoard(title: String, color: String): Result<BoardResponseDto>
    suspend fun updateBoard(
        boardUuid: String,
        update: UpdateBoardRequestDto
    ): Result<UpdateBoardRequestDto>

    suspend fun deleteBoard(boardUuid: String): Result<DeletedBoardInfoDto>
}
