package com.example.filroom.di

import android.content.Context
import com.example.filroom.data.DatastoreSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.flow.first
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {
    @Provides
    @Singleton
    fun provideHttpClient(
        datastoreSource: DatastoreSource,
        @ApplicationContext context: Context
    ) = HttpClient(Android) {
        install(ContentNegotiation) {
            gson()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
            socketTimeoutMillis = 30000
        }
        install(Auth) {
            bearer {
                loadTokens {
//                    var token =
//                        delay(1500)
                    BearerTokens(
                        accessToken = try {
                            datastoreSource.getToken().first()
                        } catch (e: NoSuchElementException) {
                            ""
                        },
                        refreshToken = ""
                    )
                }

                refreshTokens {
                    BearerTokens(
                        try {
                            datastoreSource.getToken().first()
                        } catch (e: NoSuchElementException) {
                            ""
                        },
                        ""
                    )
                }
            }
        }
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
    }
}