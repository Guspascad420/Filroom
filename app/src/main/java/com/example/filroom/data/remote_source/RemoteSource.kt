package com.example.filroom.data.remote_source

import com.example.filroom.model.request.BookingRequest
import com.example.filroom.model.request.LoginRequest
import com.example.filroom.model.request.SignUpRequest
import com.example.filroom.model.response.AuthResponse
import com.example.filroom.model.response.BookingsResponse
import com.example.filroom.model.response.CreateBookingResponse
import com.example.filroom.model.response.RoomsResponse
import com.example.filroom.model.response.UserProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class RemoteSource @Inject constructor(
    private val client: HttpClient
) {

    fun signUp(request: SignUpRequest) = getResponse {
        val res = client.post {
            url(HttpEndpoint.SIGNUP)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<AuthResponse>()

        if (res.meta.success) {
            Resource.Success(res)
        } else {
            Resource.Error(res.meta.message)
        }
    }

    fun login(request: LoginRequest) = getResponse {
        val res = client.post {
            url(HttpEndpoint.LOGIN)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<AuthResponse>()

        if (res.meta.success) {
            Resource.Success(res)
        } else {
            Resource.Error(res.meta.message)
        }
    }

    fun getAllRooms(building: String) = getResponse {
        val res = client.get {
            url("${HttpEndpoint.BASE_URL}/$building/rooms")
            contentType(ContentType.Application.Json)
        }.body<RoomsResponse>()

        if (res.meta.success) {
            Resource.Success(res)
        } else {
            Resource.Error(res.meta.message)
        }
    }

    fun getAllBookings(date: String) = getResponse {
        val res = client.get {
            url("${HttpEndpoint.BOOKINGS}?date=$date")
            contentType(ContentType.Application.Json)
        }.body<BookingsResponse>()

        if (res.meta.success) {
            Resource.Success(res)
        } else {
            Resource.Error(res.meta.message)
        }
    }

    fun createBookings(request: BookingRequest) = getResponse {
        val res = client.post {
            url("${HttpEndpoint.BOOKINGS}/create")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<CreateBookingResponse>()

        if (res.meta.success) {
            Resource.Success(res)
        } else {
            Resource.Error(res.meta.message)
        }
    }

    fun updateBookings(id: String, request: BookingRequest) = getResponse {
        val res = client.put {
            url("${HttpEndpoint.BOOKINGS}/update/$id")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<CreateBookingResponse>()

        if (res.meta.success) {
            Resource.Success(res)
        } else {
            Resource.Error(res.meta.message)
        }
    }

    fun deleteBookings(id: String) = getResponse {
        val res = client.delete {
            url("${HttpEndpoint.BOOKINGS}/delete/$id")
            contentType(ContentType.Application.Json)
        }.body<CreateBookingResponse>()

        if (res.meta.success) {
            Resource.Success(res)
        } else {
            Resource.Error(res.meta.message)
        }
    }

    fun getUserProfile() = getResponse {
        val res = client.get {
            url(HttpEndpoint.PROFILE)
            contentType(ContentType.Application.Json)
        }.body<UserProfileResponse>()

        if (res.meta.success) {
            Resource.Success(res)
        } else {
            Resource.Error(res.meta.message)
        }
    }

    fun getUserBookings(userId: Int) = getResponse {
        val res = client.get {
            url(HttpEndpoint.BOOKINGS + "?userId=$userId")
            contentType(ContentType.Application.Json)
        }.body<BookingsResponse>()

        if (res.meta.success) {
            Resource.Success(res)
        } else {
            Resource.Error(res.meta.message)
        }
    }
}