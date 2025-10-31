package com.example.sharkflow.data.local.db.dao

import androidx.room.*
import com.example.sharkflow.data.local.db.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE uuid = :uuid")
    fun getUserByUuid(uuid: String): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun clearUsers()

    @Query("SELECT * FROM users LIMIT 1")
    fun getFirstUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE isActive = 1 LIMIT 1")
    fun getActiveUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveUserOnce(): UserEntity?

    @Query("UPDATE users SET isActive = 0")
    suspend fun clearActiveUser()

}
