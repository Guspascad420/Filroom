package com.example.filroom.model.response

import com.google.gson.annotations.SerializedName

data class RoomsResponse(
    val meta: MetaResponse,
    val data: List<Room>
)

data class Room(
    @SerializedName("ID")
    val id: Int,
    @SerializedName("CreatedAt")
    val created_at: String = "",
    @SerializedName("UpdatedAt")
    val updated_at: String = "",
    @SerializedName("DeletedAt")
    val deleted_at: String = "",
    val number: Float,
    val building: String,
    val floor: Int = 0,
    val isAvailable: Boolean? = true
)