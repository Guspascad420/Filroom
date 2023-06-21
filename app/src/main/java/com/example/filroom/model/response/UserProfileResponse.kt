package com.example.filroom.model.response

data class UserProfileResponse(
    val meta: MetaResponse,
    val data: UserProfileDataResponse
)

data class UserProfileDataResponse(
    val id: Int,
    val name: String,
    val email: String,
    val username: String
)