package com.example.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.managers.AuthManager
import com.example.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authManager: AuthManager
) : ViewModel() {

    private val _loginResult = MutableStateFlow<Result<User>>(Result.success(User(0, "", "", "")))
    val loginResult = _loginResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Здесь должен быть реальный API вызов
                // Для демо создаем тестового пользователя
                val user = User(1, username, "$username@example.com", "fake_token")
                authManager.saveUser(user)
                _loginResult.value = Result.success(user)
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}