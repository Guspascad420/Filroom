package com.example.filroom.data.remote_source

object HttpEndpoint {
    const val BASE_URL = "https://gustavo-wjnt.up.railway.app"
    const val USER = "$BASE_URL/user"
    const val LOGIN = "$USER/login"
    const val SIGNUP = "$USER/register"
    const val PROFILE = "$USER/profile"
    const val BOOKINGS = "$USER/bookings"
}