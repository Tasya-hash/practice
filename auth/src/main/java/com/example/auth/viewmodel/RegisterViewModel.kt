package com.example.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.managers.AuthManager
import com.example.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authManager: AuthManager
) : ViewModel() {

    private val _registerResult = MutableStateFlow<Result<User>>(Result.success(User(0, "", "", "")))
    val registerResult = _registerResult.asStateFlow()

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                // Здесь должен быть реальный API вызов
                val user = User(System.currentTimeMillis(), username, email, "fake_token")
                authManager.saveUser(user)
                _registerResult.value = Result.success(user)
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }
}