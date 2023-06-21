package com.example.filroom.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.datastore: DataStore<Preferences> by preferencesDataStore("settings")

class DatastoreSource @Inject constructor(
    context: Context
) {
    private val datastore = context.datastore
    suspend fun setToken(token: String) {
        datastore.edit {
            it[stringPreferencesKey("accessToken")] = token
        }
    }

    fun getToken() = datastore.data.map {
        it[stringPreferencesKey("accessToken")] ?: ""
    }
}