package com.example.filroom.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class BookingsResponse(
    val meta: MetaResponse,
    val data: List<Booking>
)

data class CreateBookingResponse(
    val meta: MetaResponse,
    val data: Nothing? = null
)

data class Booking(
    val id: Int,
    val name: String,
    val date: String,
    val time: String,
    val requirement: String,
    val room_id: Int,
    val room: Room
)