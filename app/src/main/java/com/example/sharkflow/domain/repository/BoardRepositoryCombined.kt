package com.example.sharkflow.domain.repository

import com.example.sharkflow.data.api.dto.board.*
import com.example.sharkflow.domain.model.Board
import kotlinx.coroutines.flow.Flow

interface BoardRepositoryCombined {
    fun getBoardsFlow(): Flow<List<Board>>
    suspend fun refreshBoards()
    suspend fun createBoard(title: String, color: String, localUuid: String? = null): Result<Board>
    suspend fun updateBoard(boardUuid: String, update: UpdateBoardRequestDto): Result<Board>
    suspend fun deleteBoard(boardUuid: String, hardDelete: Boolean): Result<DeletedBoardInfoDto>
    suspend fun getAllBoards(): List<Board>
    suspend fun getUnsyncedBoards(): List<Board>
    suspend fun getDeletedBoards(): List<Board>
}
