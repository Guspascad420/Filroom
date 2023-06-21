package com.example.filroom.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filroom.data.DatastoreSource
import com.example.filroom.data.remote_source.RemoteSource
import com.example.filroom.data.remote_source.Resource
import com.example.filroom.model.request.LoginRequest
import com.example.filroom.model.request.SignUpRequest
import com.example.filroom.model.response.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val datastoreSource: DatastoreSource,
    private val remoteSource: RemoteSource
) : ViewModel() {
    var name by mutableStateOf("")
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isPasswordVisible by mutableStateOf(false)
    var error by mutableStateOf(false)

    val loginState = MutableStateFlow<Resource<AuthResponse>>(Resource.Loading())
    val signUpState = MutableStateFlow<Resource<AuthResponse>>(Resource.Loading())

    fun getToken() = datastoreSource.getToken()
    fun login() {
        viewModelScope.launch {
            remoteSource.login(LoginRequest(email, password)).collect {
                loginState.value = it
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            remoteSource.signUp(SignUpRequest(name, username, email, password)).collect {
                signUpState.value = it
            }
        }
    }

    suspend fun saveToken(token: String) = datastoreSource.setToken(token)
}