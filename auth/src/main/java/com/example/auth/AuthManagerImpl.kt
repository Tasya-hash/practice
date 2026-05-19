package com.example.auth

import android.content.Context
import com.example.auth.data.repository.AuthRepository
import com.example.auth.utils.TokenManager
import com.example.domain.interfaces.AuthManager
import com.example.domain.models.AuthState
import com.example.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthManagerImpl(
    private val context: Context,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : AuthManager {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    override fun observeAuthState(): Flow<AuthState> = _authState.asStateFlow()

    override fun getCurrentUser(): User? {
        val userId = tokenManager.getUserId()
        val email = tokenManager.getUserEmail()
        return if (userId != -1L && email != null) {
            User(id = userId, email = email, username = email.split("@")[0])
        } else null
    }

    override fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()

    override fun logout() {
        tokenManager.clear()
        _authState.value = AuthState.Unauthenticated
    }

    override suspend fun login(email: String, password: String): Result<User> {
        _authState.value = AuthState.Loading(true)
        return try {
            val result = authRepository.login(email, password)
            result.fold(
                onSuccess = { authResponse ->
                    _authState.value = AuthState.Authenticated
                    return Result.success(User(
                        id = authResponse.userId,
                        email = authResponse.email,
                        username = authResponse.username
                    ))
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Login failed")
                    Result.failure(exception)
                }
            )
        } finally {
            _authState.value = AuthState.Loading(false)
        }
    }

    override suspend fun register(email: String, username: String, password: String): Result<User> {
        _authState.value = AuthState.Loading(true)
        return try {
            val result = authRepository.register(email, username, password)
            result.fold(
                onSuccess = { authResponse ->
                    _authState.value = AuthState.Authenticated
                    Result.success(User(
                        id = authResponse.userId,
                        email = authResponse.email,
                        username = authResponse.username
                    ))
                },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Registration failed")
                    Result.failure(exception)
                }
            )
        } finally {
            _authState.value = AuthState.Loading(false)
        }
    }
}