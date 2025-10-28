package com.example.sharkflow.di

import android.content.Context
import android.os.Build
import com.example.sharkflow.BuildConfig
import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.data.api.*
import com.example.sharkflow.data.local.preference.DeviceIdPreference
import com.example.sharkflow.data.network.*
import com.example.sharkflow.domain.repository.TokenRepository
import com.google.gson.*
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.Instant
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Instant::class.java, JsonDeserializer { json, _, _ ->
                Instant.parse(json.asString)
            })
            .registerTypeAdapter(Instant::class.java, JsonSerializer<Instant> { src, _, _ ->
                JsonPrimitive(src.toString())
            })
            .create()
    }

    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Provides
    @Singleton
    fun provideCookieJar(@ApplicationContext context: Context): CookieJar {
        return SecureCookieJar(context)
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        tokenRepository: TokenRepository,
        baseUrl: String,
        @Named("refreshClient") refreshClient: OkHttpClient
    ): TokenAuthenticator = TokenAuthenticator(tokenRepository, baseUrl, refreshClient)

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenRepository: TokenRepository,
    ): AuthInterceptor {
        return AuthInterceptor(tokenRepository)
    }


    @Provides
    @Singleton
    fun provideBaseClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        deviceIdPreference: DeviceIdPreference,
        cookieJar: CookieJar
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message -> AppLog.d("HTTP_LOG", message) }
            .apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BODY
            }

        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .header(
                        "User-Agent",
                        "SharkFlow/1.0 (Android ${Build.VERSION.RELEASE}; ${Build.MANUFACTURER} ${Build.MODEL})"
                    )
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(authInterceptor)
            .addInterceptor(DeviceIdInterceptor(deviceIdPreference))
            .addInterceptor(loggingInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("refreshClient")
    fun provideRefreshClient(
        authInterceptor: AuthInterceptor,
        cookieJar: CookieJar,
        deviceIdPreference: DeviceIdPreference
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .header(
                        "User-Agent",
                        "SharkFlow/1.0 (Android ${Build.VERSION.RELEASE}; ${Build.MANUFACTURER} ${Build.MODEL})"
                    )
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(authInterceptor)
            .addInterceptor(DeviceIdInterceptor(deviceIdPreference))
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

    @Provides
    @Singleton
    fun provideBoardApi(retrofit: Retrofit): BoardApi = retrofit.create(BoardApi::class.java)

    @Provides
    @Singleton
    fun provideTaskApi(retrofit: Retrofit): TaskApi = retrofit.create(TaskApi::class.java)
}