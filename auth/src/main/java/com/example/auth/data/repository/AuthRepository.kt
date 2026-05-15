package com.example.auth.data.repository

import com.example.auth.data.models.AuthResponse
import com.example.auth.data.remote.RetrofitClient
import com.example.auth.data.TokenManager
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val tokenManager: TokenManager
) {

    private val apiService = RetrofitClient.getApiService(tokenManager)

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(com.example.auth.data.models.AuthRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    Result.success(authResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP error: ${e.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, username: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.register(com.example.auth.data.models.RegisterRequest(email, username, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    Result.success(authResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP error: ${e.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}