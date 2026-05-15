package com.example.domain

data class User(
    val id: Long,
    val email: String,
    val username: String,
    val firstName: String? = null,
    val lastName: String? = null
)