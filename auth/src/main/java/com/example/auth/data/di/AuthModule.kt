package com.example.auth.data.di

import android.content.Context
import com.example.auth.AuthManagerImpl
import com.example.auth.AuthNavigatorImpl
import com.example.auth.AuthViewModel
import com.example.auth.data.TokenManager
import com.example.auth.data.repository.AuthRepository
import com.example.domain.AuthManager
import com.example.domain.AuthNavigator

class AuthModule(private val context: Context) {

    private val tokenManager by lazy { TokenManager(context) }
    private val authRepository by lazy { AuthRepository(tokenManager) }
    private val authManager by lazy { AuthManagerImpl(context) }

    fun provideAuthManager(): AuthManager = authManager

    fun provideAuthNavigator(): AuthNavigator = AuthNavigatorImpl()

    fun provideAuthViewModel(): AuthViewModel {
        return AuthViewModel(authRepository, authManager as AuthManagerImpl)
    }
}