package com.example.filroom.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filroom.data.remote_source.RemoteSource
import com.example.filroom.data.remote_source.Resource
import com.example.filroom.model.request.BookingRequest
import com.example.filroom.model.response.AuthResponse
import com.example.filroom.model.response.CreateBookingResponse
import com.example.filroom.model.response.UserProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val remoteSource: RemoteSource
) : ViewModel() {
    var name by mutableStateOf("")
    var date by mutableStateOf(LocalDate.now().plusDays(1).toString())
    var startTime by mutableStateOf("Waktu Mulai")
    var endTime by mutableStateOf("Waktu Selesai")
    var requirement by mutableStateOf("")
    var startTimeExpanded by mutableStateOf(false)
    var endTimeExpanded by mutableStateOf(false)
    var roomId by mutableStateOf(0)
    var userId by mutableStateOf(0)
    var openDialog by mutableStateOf(false)

    val createBookingsState = MutableStateFlow<Resource<CreateBookingResponse>>(Resource.Loading())
    val updateBookingsState = MutableStateFlow<Resource<CreateBookingResponse>>(Resource.Loading())
    val deleteBookingsState = MutableStateFlow<Resource<CreateBookingResponse>>(Resource.Loading())
    val userProfileState = MutableStateFlow<Resource<UserProfileResponse>>(Resource.Loading())

    fun getUserProfile() {
        viewModelScope.launch {
            remoteSource.getUserProfile().collect {
                userProfileState.value = it
            }
        }
    }

    fun createBookings() {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("'T'HH:mm:ss'Z'")
            val timeZone = ZonedDateTime.now().format(formatter)
            val requestDate = date + timeZone

            val bookingRequest = BookingRequest(
                name, requestDate, "$startTime - $endTime", requirement, roomId, userId
            )
            remoteSource.createBookings(bookingRequest).collect {
                createBookingsState.value = it
            }
        }
    }

    fun updateBookings(id: String) {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("'T'HH:mm:ss'Z'")
            val timeZone = ZonedDateTime.now().format(formatter)
            val requestDate = date + timeZone

            val bookingRequest = BookingRequest(
                name, requestDate, "$startTime - $endTime", requirement, roomId, userId
            )
            remoteSource.updateBookings(id, bookingRequest).collect {
                updateBookingsState.value = it
            }
        }
    }

    fun deleteBookings(id: String) {
        viewModelScope.launch {
            remoteSource.deleteBookings(id).collect {
                deleteBookingsState.value = it
            }
        }
    }
}