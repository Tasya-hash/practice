package com.example.domain

import kotlinx.coroutines.flow.StateFlow

interface AuthManager {
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    fun logout()
    fun observeAuthState(): StateFlow<AuthState>
    fun getCurrentUserId(): Long
}