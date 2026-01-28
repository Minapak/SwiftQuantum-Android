package com.swiftquantum.di

import com.swiftquantum.BuildConfig
import com.swiftquantum.data.api.AuthApi
import com.swiftquantum.data.api.BridgeApi
import com.swiftquantum.data.api.QuantumApi
import com.swiftquantum.data.local.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = false
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    @Named("AuthInterceptor")
    fun provideAuthInterceptor(tokenManager: TokenManager): Interceptor {
        return Interceptor { chain ->
            val token = runBlocking { tokenManager.getAccessToken() }
            val request = chain.request().newBuilder().apply {
                token?.let {
                    addHeader("Authorization", "Bearer $it")
                }
                addHeader("Content-Type", "application/json")
                addHeader("Accept", "application/json")
            }.build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    @Named("ApiClient")
    fun provideApiOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @Named("AuthInterceptor") authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    @Named("BridgeClient")
    fun provideBridgeOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @Named("AuthInterceptor") authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS) // Longer timeout for hardware operations
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    @Named("ApiRetrofit")
    fun provideApiRetrofit(
        @Named("ApiClient") okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    @Named("BridgeRetrofit")
    fun provideBridgeRetrofit(
        @Named("BridgeClient") okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BRIDGE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(@Named("ApiRetrofit") retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideQuantumApi(@Named("ApiRetrofit") retrofit: Retrofit): QuantumApi {
        return retrofit.create(QuantumApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBridgeApi(@Named("BridgeRetrofit") retrofit: Retrofit): BridgeApi {
        return retrofit.create(BridgeApi::class.java)
    }
}
