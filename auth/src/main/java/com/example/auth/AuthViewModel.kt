package com.example.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val authManager: AuthManagerImpl
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<Boolean>>()
    val loginResult: LiveData<Result<Boolean>> = _loginResult

    private val _registerResult = MutableLiveData<Result<Boolean>>()
    val registerResult: LiveData<Result<Boolean>> = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            authManager.setLoading(true)
            val result = authRepository.login(email, password)
            result.onSuccess { response ->
                authManager.onLoginSuccess(
                    userId = response.userId,
                    email = response.email,
                    username = response.username,
                    token = response.token
                )
                _loginResult.value = Result.success(true)
            }.onFailure { exception ->
                authManager.setError(exception.message ?: "Ошибка входа")
                _loginResult.value = Result.failure(exception)
            }
            _isLoading.value = false
            authManager.setLoading(false)
        }
    }

    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            authManager.setLoading(true)
            val result = authRepository.register(email, username, password)
            result.onSuccess { response ->
                authManager.onLoginSuccess(
                    userId = response.userId,
                    email = response.email,
                    username = response.username,
                    token = response.token
                )
                _registerResult.value = Result.success(true)
            }.onFailure { exception ->
                authManager.setError(exception.message ?: "Ошибка регистрации")
                _registerResult.value = Result.failure(exception)
            }
            _isLoading.value = false
            authManager.setLoading(false)
        }
    }

    fun isLoggedIn(): Boolean = authManager.isLoggedIn()
}
