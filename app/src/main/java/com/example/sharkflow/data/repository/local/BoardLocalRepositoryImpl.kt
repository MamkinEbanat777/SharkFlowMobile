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
        boardDao.deleteBoard(board)
    }

    suspend fun clearBoardsForUser(userUuid: String) {
        boardDao.clearBoardsForUser(userUuid)
    }

    suspend fun getBoardsOnce(userUuid: String): List<BoardEntity> =
        boardDao.getBoardsForUserOnce(userUuid)

    suspend fun getByServerUuid(serverUuid: String): BoardEntity? =
        boardDao.getByServerUuid(serverUuid)

    suspend fun getByLocalUuid(localUuid: String): BoardEntity? =
        boardDao.getByLocalUuid(localUuid)

    suspend fun getUnsyncedBoards(): List<BoardEntity> =
        boardDao.getUnsyncedBoards()

    suspend fun getDeletedBoards(): List<BoardEntity> =
        boardDao.getDeletedBoards()

    suspend fun getAllBoards(): List<BoardEntity> =
        boardDao.getAllBoards()

}
