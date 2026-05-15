package com.example.auth.di

import android.content.Context
import com.example.auth.manager.AuthManagerImpl
import com.example.auth.viewmodel.AuthViewModel
import com.example.auth.viewmodel.RegisterViewModel
import com.example.domain.managers.AuthManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<AuthManager> { AuthManagerImpl(androidContext()) }
    viewModel { AuthViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}