package com.example.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.data.repository.AuthRepository
import com.example.auth.data.repository.AuthRepositoryImpl
import com.example.auth.data.remote.RetrofitClient
import com.example.auth.utils.TokenManager
import com.example.domain.models.User
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private lateinit var authRepository: AuthRepository
    private lateinit var tokenManager: TokenManager

    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> = _loginResult

    private val _registerResult = MutableLiveData<Result<User>>()
    val registerResult: LiveData<Result<User>> = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Инициализация должна быть вызвана из Activity
    fun init(context: android.content.Context) {
        tokenManager = TokenManager(context.applicationContext)
        val apiService = RetrofitClient.getApiService(tokenManager)
        authRepository = AuthRepositoryImpl(apiService, tokenManager)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.login(email, password)
            _loginResult.value = result.map { authResponse ->
                User(
                    id = authResponse.userId,
                    email = authResponse.email,
                    username = authResponse.username
                )
            }
            _isLoading.value = false
        }
    }

    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.register(email, username, password)
            _registerResult.value = result.map { authResponse ->
                User(
                    id = authResponse.userId,
                    email = authResponse.email,
                    username = authResponse.username
                )
            }
            _isLoading.value = false
        }
    }

    fun isLoggedIn(): Boolean {
        return ::tokenManager.isInitialized && tokenManager.isLoggedIn()
    }

    fun logout() {
        if (::tokenManager.isInitialized) {
            tokenManager.clear()
        }
    }
}