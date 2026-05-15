package com.example.auth.data.remote

import com.example.auth.data.models.AuthRequest
import com.example.auth.data.models.AuthResponse
import com.example.auth.data.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
}