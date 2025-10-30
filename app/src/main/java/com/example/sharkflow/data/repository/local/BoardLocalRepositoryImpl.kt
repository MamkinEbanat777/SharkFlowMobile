package com.example.sharkflow.data.repository.local

import com.example.sharkflow.data.local.db.dao.BoardDao
import com.example.sharkflow.data.local.db.entities.BoardEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class BoardLocalRepositoryImpl @Inject constructor(
    private val boardDao: BoardDao
) {
    suspend fun deleteBoardByUuid(boardUuid: String) {
        val board = boardDao.getBoardByUuid(boardUuid) ?: return
        boardDao.updateBoard(board.copy(isDeleted = true))
    }

    fun getBoardsFlow(userUuid: String): Flow<List<BoardEntity>> =
        boardDao.getBoardsForUser(userUuid)

    suspend fun insertOrUpdateBoards(boards: List<BoardEntity>) {
        boardDao.insertBoards(boards)
    }

    suspend fun updateBoard(board: BoardEntity) {
        boardDao.updateBoard(board)
    }

    suspend fun deleteBoard(board: BoardEntity) {
        val deletedBoard = board.copy(isDeleted = true)
        boardDao.updateBoard(deletedBoard)
    }

    suspend fun clearBoardsForUser(userUuid: String) {
        boardDao.clearBoardsForUser(userUuid)
    }


}
