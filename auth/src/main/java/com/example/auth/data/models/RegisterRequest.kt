package com.example.auth.data.models

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)