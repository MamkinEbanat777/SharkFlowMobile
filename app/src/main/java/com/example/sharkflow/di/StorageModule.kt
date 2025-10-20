package com.example.sharkflow.di


import android.content.Context
import com.example.sharkflow.data.local.SecureTokenPreference
import com.example.sharkflow.data.repository.SecureTokenPreferenceImpl
import com.example.sharkflow.data.storage.TokenStorage
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideTokenStorage(@ApplicationContext context: Context): TokenStorage {
        return SecureTokenPreferenceImpl(context)
    }

    @Provides
    @Singleton
    fun provideSecureTokenStorage(): SecureTokenPreference {
        return SecureTokenPreference
    }
}
