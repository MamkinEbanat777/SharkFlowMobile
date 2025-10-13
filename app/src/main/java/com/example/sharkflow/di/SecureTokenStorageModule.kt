package com.example.sharkflow.di


import android.content.*
import com.example.sharkflow.data.local.*
import com.example.sharkflow.data.repository.*
import com.example.sharkflow.data.storage.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.android.qualifiers.*
import dagger.hilt.components.*
import jakarta.inject.*

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
    fun provideTokenRepository(storage: TokenStorage): TokenRepository {
        return TokenRepository(storage)
    }
}

