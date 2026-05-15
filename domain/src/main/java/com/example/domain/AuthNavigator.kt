package com.example.domain

interface AuthNavigator {
    fun navigateToLogin()
    fun navigateToRegister()
    fun openAuthFlow()
    fun navigateToMainAfterAuth()
}