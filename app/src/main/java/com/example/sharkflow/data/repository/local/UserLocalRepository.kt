package com.example.sharkflow.data.repository.local

import com.example.sharkflow.data.local.db.dao.UserDao
import com.example.sharkflow.data.mapper.UserMapper
import com.example.sharkflow.domain.model.User
import com.example.sharkflow.utils.AppLog
import jakarta.inject.*
import kotlinx.coroutines.flow.*

@Singleton
class UserLocalRepository @Inject constructor(
    private val userDao: UserDao
) {
    fun getUserFlow(uuid: String): Flow<User?> =
        userDao.getUserByUuid(uuid).map { it?.let { UserMapper.toDomain(it) } }

    suspend fun getUserOnce(uuid: String): User? =
        userDao.getUserByUuid(uuid).firstOrNull()?.let { UserMapper.toDomain(it) }

    suspend fun insertOrUpdate(user: User?) {
        if (user == null) return
        AppLog.i("User added: $user")
        userDao.insertUser(UserMapper.fromDomain(user))
    }

    suspend fun delete(user: User) {
        AppLog.i("User delete: $user")
        userDao.deleteUser(UserMapper.fromDomain(user))
    }

    suspend fun clearAll() = userDao.clearUsers()
}