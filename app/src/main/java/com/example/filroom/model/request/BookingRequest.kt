package com.example.filroom.model.request

data class BookingRequest(
    val name: String,
    val date: String,
    val time: String,
    val requirement: String,
    val room_id: Int,
    val user_id: Int
)