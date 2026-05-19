package com.example.auth.data.repository

import com.example.auth.data.models.AuthRequest
import com.example.auth.data.models.RegisterRequest
import com.example.auth.data.remote.ApiService
import com.example.auth.data.models.AuthResponse
import com.example.auth.utils.TokenManager
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(AuthRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    tokenManager.saveToken(authResponse.token)
                    tokenManager.saveUserId(authResponse.userId)
                    tokenManager.saveUserEmail(authResponse.email)
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

    override suspend fun register(email: String, username: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.register(RegisterRequest(email, username, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    tokenManager.saveToken(authResponse.token)
                    tokenManager.saveUserId(authResponse.userId)
                    tokenManager.saveUserEmail(authResponse.email)
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