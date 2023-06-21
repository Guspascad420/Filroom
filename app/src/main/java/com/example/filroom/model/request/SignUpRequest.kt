package com.example.filroom.model.request

data class SignUpRequest(
    val name: String,
    val username: String,
    val email: String,
    val password: String
)