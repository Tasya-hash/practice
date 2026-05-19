package com.example.auth.data.repository

import com.example.auth.data.models.AuthRequest
import com.example.auth.data.models.AuthResponse
import com.example.auth.data.models.RegisterRequest
import retrofit2.Response

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResponse>
    suspend fun register(email: String, username: String, password: String): Result<AuthResponse>
}
