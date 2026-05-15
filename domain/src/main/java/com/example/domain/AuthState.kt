package com.example.domain

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    data class Loading(val isLoading: Boolean) : AuthState()
    data class Error(val message: String) : AuthState()
}