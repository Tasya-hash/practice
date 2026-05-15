package com.example.domain.models

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val token: String
)