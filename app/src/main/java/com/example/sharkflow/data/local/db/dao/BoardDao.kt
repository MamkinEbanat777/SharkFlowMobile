package com.example.sharkflow.data.local.db.dao

import androidx.room.*
import com.example.sharkflow.data.local.db.entities.BoardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardDao {
    @Query("SELECT * FROM boards WHERE userId = :userId AND isDeleted = 0")
    fun getBoardsForUser(userId: Int): Flow<List<BoardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoards(boards: List<BoardEntity>)

    @Update
    suspend fun updateBoard(board: BoardEntity)

    @Query("DELETE FROM boards WHERE userId = :userId")
    suspend fun clearBoardsForUser(userId: Int)
}
