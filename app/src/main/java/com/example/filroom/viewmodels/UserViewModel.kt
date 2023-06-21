package com.example.filroom.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filroom.data.remote_source.RemoteSource
import com.example.filroom.data.remote_source.Resource
import com.example.filroom.model.response.BookingsResponse
import com.example.filroom.model.response.UserProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val remoteSource: RemoteSource
): ViewModel() {
    val userProfileState = MutableStateFlow<Resource<UserProfileResponse>>(Resource.Loading())
    val userBookingsState = MutableStateFlow<Resource<BookingsResponse>>(Resource.Loading())

    fun getUserProfile() {
        viewModelScope.launch {
            remoteSource.getUserProfile().collect {
                userProfileState.value = it
            }
        }
    }

    fun getUserBookings(userId: Int) {
        viewModelScope.launch {
            remoteSource.getUserBookings(userId).collect {
                userBookingsState.value = it
            }
        }
    }
}