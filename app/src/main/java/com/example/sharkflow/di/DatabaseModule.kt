package com.example.sharkflow.di

import android.content.Context
import androidx.room.Room
import com.example.sharkflow.data.local.db.AppDatabase
import com.example.sharkflow.data.local.db.dao.*
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sharkflow.db"
        ).build()

    @Provides
    fun provideBoardDao(db: AppDatabase): BoardDao = db.boardDao()

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
    
}
