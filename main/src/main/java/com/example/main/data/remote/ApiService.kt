package com.example.main.data.remote

import com.example.main.data.models.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/api/users")
    suspend fun getUsers(): Response<List<User>>

    @GET("/api/users/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<User>
}