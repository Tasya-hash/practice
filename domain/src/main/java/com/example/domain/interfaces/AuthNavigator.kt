package com.example.domain.interfaces

interface AuthNavigator {
    fun navigateToLogin()
    fun navigateToRegister()
    fun openAuthFlow()
    fun navigateToMainAfterAuth()
}