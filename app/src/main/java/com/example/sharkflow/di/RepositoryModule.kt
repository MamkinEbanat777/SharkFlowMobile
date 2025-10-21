package com.example.sharkflow.di

import com.example.sharkflow.data.repository.remote.*
import com.example.sharkflow.domain.repository.*
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindRegisterRepository(
        impl: RegisterRepositoryImpl
    ): RegisterRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindCloudinaryRepository(
        impl: CloudinaryRepositoryImpl
    ): CloudinaryRepository

    @Binds
    @Singleton
    abstract fun bindTokenRepository(
        impl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    @Singleton
    abstract fun bindLanguageRepository(
        impl: LanguageRepositoryImpl
    ): LanguageRepository

    @Binds
    @Singleton
    abstract fun bindThemeRepository(
        impl: ThemeRepositoryImpl
    ): ThemeRepository

}
