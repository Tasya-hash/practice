package com.example.domain.managers

import com.example.domain.models.AuthState
import com.example.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AuthManager {
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    suspend fun logout()
    fun observeAuthState(): Flow<AuthState>
    suspend fun saveUser(user: User)
    fun getUserId(): Long?
}