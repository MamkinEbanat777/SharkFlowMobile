package com.example.sharkflow.di

import android.content.Context
import com.example.sharkflow.BuildConfig
import com.example.sharkflow.data.api.*
import com.example.sharkflow.data.local.DeviceIdPreference
import com.example.sharkflow.data.network.*
import com.example.sharkflow.domain.repository.TokenRepository
import com.example.sharkflow.utils.AppLog
import com.google.gson.Gson
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenRepository: TokenRepository,
        baseUrl: String,
    ): AuthInterceptor {
        return AuthInterceptor(tokenRepository, baseUrl)
    }

    @Provides
    @Singleton
    fun provideBaseClient(
        authInterceptor: AuthInterceptor,
        @ApplicationContext context: Context,
        deviceIdPreference: DeviceIdPreference
    ): OkHttpClient {
        val cookieJar = SecureCookieJar(context)
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            AppLog.d("HTTP_LOG", message)
        }.apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(authInterceptor)
            .addInterceptor(DeviceIdInterceptor(deviceIdPreference))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)
}