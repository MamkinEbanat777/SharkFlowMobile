package com.example.sharkflow.data.local.db.dao

import androidx.room.*
import com.example.sharkflow.data.local.db.entities.BoardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardDao {
    @Query("SELECT * FROM boards WHERE userUuid = :userUuid AND isDeleted = 0")
    fun getBoardsForUser(userUuid: String): Flow<List<BoardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoards(boards: List<BoardEntity>)

    @Update
    suspend fun updateBoard(board: BoardEntity)

    @Query("DELETE FROM boards WHERE userUuid = :userUuid")
    suspend fun clearBoardsForUser(userUuid: String)

    @Query("SELECT * FROM boards WHERE uuid = :uuid")
    suspend fun getBoardByUuid(uuid: String): BoardEntity?

    @Query("SELECT * FROM boards WHERE userUuid = :userUuid AND isDeleted = 0")
    suspend fun getBoardsForUserOnce(userUuid: String): List<BoardEntity>

    @Query("SELECT * FROM boards WHERE serverUuid = :serverUuid LIMIT 1")
    suspend fun getByServerUuid(serverUuid: String): BoardEntity?

    @Query("SELECT * FROM boards WHERE uuid = :localUuid LIMIT 1")
    suspend fun getByLocalUuid(localUuid: String): BoardEntity?

    @Query("SELECT * FROM boards WHERE isSynced = 0")
    suspend fun getUnsyncedBoards(): List<BoardEntity>

    @Query("SELECT * FROM boards WHERE isDeleted = 1 AND isSynced = 0")
    suspend fun getDeletedBoards(): List<BoardEntity>


    @Query("SELECT * FROM boards")
    suspend fun getAllBoards(): List<BoardEntity>

}
