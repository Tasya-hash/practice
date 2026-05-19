package com.example.auth.data.remote

import com.example.auth.data.models.*
import retrofit2.http.*
import retrofit2.Response
import com.example.domain.models.User

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("/api/users")
    suspend fun getUsers(): Response<List<User>>

    @GET("/api/users/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<User>
}
