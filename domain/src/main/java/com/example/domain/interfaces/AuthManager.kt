package com.example.domain.interfaces

import com.example.domain.models.AuthState
import com.example.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AuthManager {
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    fun logout()
    fun observeAuthState(): Flow<AuthState>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(email: String, username: String, password: String): Result<User>
}