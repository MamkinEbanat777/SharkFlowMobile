package com.example.sharkflow.di

import android.content.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.android.qualifiers.*
import dagger.hilt.components.*
import jakarta.inject.*

@Module
@InstallIn(SingletonComponent::class)
object ContextModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context
}