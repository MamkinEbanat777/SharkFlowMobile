package com.example.sharkflow.data.local.db

import androidx.room.*
import com.example.sharkflow.data.local.Converters
import com.example.sharkflow.data.local.db.dao.*
import com.example.sharkflow.data.local.db.entities.*

@Database(
    entities = [UserEntity::class, BoardEntity::class, TaskEntity::class],
    version = 9,
    exportSchema = true
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun boardDao(): BoardDao
    abstract fun taskDao(): TaskDao
}
