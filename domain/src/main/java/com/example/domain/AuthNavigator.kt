package com.example.domain

interface AuthNavigator {
    fun navigateToLogin()
    fun navigateToRegister()
    fun openAuthFlow(requestCode: Int)
    fun navigateToMainAfterAuth()
}
