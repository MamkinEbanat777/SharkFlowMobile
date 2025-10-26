package com.example.sharkflow.domain.repository

import com.example.sharkflow.data.api.dto.board.*
import com.example.sharkflow.domain.model.Board
import kotlinx.coroutines.flow.Flow

interface BoardRepositoryCombined {
    fun getBoardsFlow(userId: Int): Flow<List<Board>>
    suspend fun refreshBoards(userId: Int)
    suspend fun createBoard(title: String, color: String): Result<Board>
    suspend fun updateBoard(boardUuid: String, update: UpdateBoardRequestDto): Result<Board>
    suspend fun deleteBoard(boardUuid: String): Result<DeletedBoardInfoDto>
}
