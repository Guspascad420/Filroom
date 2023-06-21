package com.example.filroom.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filroom.data.remote_source.RemoteSource
import com.example.filroom.data.remote_source.Resource
import com.example.filroom.model.response.BookingsResponse
import com.example.filroom.model.response.RoomsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val remoteSource: RemoteSource
) : ViewModel() {
    val roomsState = MutableStateFlow<Resource<RoomsResponse>>(Resource.Loading())
    val bookingsState = MutableStateFlow<Resource<BookingsResponse>>(Resource.Loading())

    var expanded by mutableStateOf(false)
    var selectedFloor by mutableStateOf(2)

    fun getAllRooms(building: String) {
        viewModelScope.launch {
            remoteSource.getAllRooms(building).collect {
                roomsState.value = it
            }
        }
    }

    fun getAllBookings(date: String) {
        viewModelScope.launch {
            remoteSource.getAllBookings(date).collect {
                bookingsState.value = it
            }
        }
    }
}