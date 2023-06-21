package com.example.filroom.model.response

data class AuthResponse(
    val meta:MetaResponse,
    val data:AuthDataResponse
)

data class AuthDataResponse(
    val token:String
)