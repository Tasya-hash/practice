package com.example.auth

import android.content.Context
import com.example.domain.AuthManager
import com.example.domain.AuthState
import com.example.domain.User
import com.example.auth.data.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AuthManagerImpl(private val context: Context) : AuthManager {

    private val tokenManager = TokenManager(context)
    private val _authState = MutableStateFlow<AuthState>(if (tokenManager.isLoggedIn()) AuthState.Authenticated else AuthState.Unauthenticated)

    override fun getCurrentUser(): User? {
        return if (isLoggedIn()) {
            User(
                id = tokenManager.getUserId(),
                email = tokenManager.getUserEmail() ?: "",
                username = tokenManager.getUsername() ?: ""
            )
        } else null
    }

    override fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()

    override fun logout() {
        tokenManager.clear()
        _authState.value = AuthState.Unauthenticated
    }

    override fun observeAuthState(): Flow<AuthState> = _authState

    override fun getCurrentUserId(): Long = tokenManager.getUserId()

    fun onLoginSuccess(userId: Long, email: String, username: String, token: String) {
        tokenManager.saveToken(token)
        tokenManager.saveUserId(userId)
        tokenManager.saveUserEmail(email)
        tokenManager.saveUsername(username)
        _authState.value = AuthState.Authenticated
    }

    fun setLoading(isLoading: Boolean) {
        _authState.value = AuthState.Loading(isLoading)
    }

    fun setError(message: String) {
        _authState.value = AuthState.Error(message)
    }
}