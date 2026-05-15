package com.example.auth.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.domain.managers.AuthManager
import com.example.domain.models.AuthState
import com.example.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.gson.Gson

class AuthManagerImpl(private val context: Context) : AuthManager {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    override fun observeAuthState(): Flow<AuthState> = _authState.asStateFlow()

    init {
        val user = getCurrentUser()
        _authState.value = if (user != null) AuthState.Authenticated else AuthState.Unauthenticated
    }

    override fun getCurrentUser(): User? {
        val userJson = prefs.getString("current_user", null)
        return userJson?.let { gson.fromJson(it, User::class.java) }
    }

    override fun isLoggedIn(): Boolean = getCurrentUser() != null

    override suspend fun logout() {
        prefs.edit().clear().apply()
        _authState.value = AuthState.Unauthenticated
    }

    override suspend fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        prefs.edit().putString("current_user", userJson).apply()
        _authState.value = AuthState.Authenticated
    }

    override fun getUserId(): Long? = getCurrentUser()?.id
}