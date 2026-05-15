package com.example.auth.di

import android.content.Context
import com.example.auth.manager.AuthManagerImpl
import com.example.auth.repository.AuthRepository
import com.example.auth.network.AuthApiService
import com.example.auth.network.AuthInterceptor
import com.example.auth.network.TokenManager
import com.example.domain.managers.AuthManager
import com.example.domain.navigation.AuthNavigator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val authModule = module {
    single<AuthNavigator> { AuthNavigatorImpl() }
    single<AuthManager> { AuthManagerImpl(androidContext()) }

    single { TokenManager(androidContext()) }
    single { AuthInterceptor(get()) }

    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(get<AuthInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://your-api-base-url.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    single { AuthRepository(get(), get()) }
}