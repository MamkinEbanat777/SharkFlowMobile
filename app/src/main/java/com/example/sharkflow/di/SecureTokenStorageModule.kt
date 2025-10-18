package com.example.sharkflow.di


import android.content.Context
import com.example.sharkflow.data.local.token.*
import com.example.sharkflow.data.manager.TokenManager
import com.example.sharkflow.data.repository.TokenRepository
import com.example.sharkflow.data.storage.TokenStorage
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecureTokenStorageModule {

    @Provides
    @Singleton
    fun provideTokenStorage(@ApplicationContext context: Context): TokenStorage {
        return SecureTokenStorageImpl(context)
    }

    @Provides
    @Singleton
    fun provideSecureTokenStorage(@ApplicationContext context: Context): SecureTokenStorage {
        return SecureTokenStorage
    }

    @Provides
    @Singleton
    fun provideTokenRepository(
        storage: TokenStorage,
        tokenManager: TokenManager
    ): TokenRepository {
        return TokenRepository(storage, tokenManager)
    }
}

