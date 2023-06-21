package com.example.filroom

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.filroom.ui.screens.BookingScreen
import com.example.filroom.ui.screens.BuildingsScreen
import com.example.filroom.ui.screens.EditBookingScreen
import com.example.filroom.ui.screens.LoginScreen
import com.example.filroom.ui.screens.RoomsSchedule
import com.example.filroom.ui.screens.RoomsScreen
import com.example.filroom.ui.screens.SignUpScreen
import com.example.filroom.ui.screens.StatusDetailScreen
import com.example.filroom.ui.screens.StatusesScreen
import com.example.filroom.ui.theme.FilroomTheme
import com.example.filroom.utils.NavRoute
import com.example.filroom.viewmodels.RoomViewModel
import com.example.filroom.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            FilroomTheme {
                val roomViewModel = hiltViewModel<RoomViewModel>()
                val userViewModel = hiltViewModel<UserViewModel>()
                NavHost(navController = navController, startDestination = NavRoute.LOGIN.name) {
                    composable(NavRoute.LOGIN.name) {
                        LoginScreen(navController)
                    }
                    composable(NavRoute.BUILDINGS.name) {
                        BuildingsScreen(navController)
                    }
                    composable(NavRoute.ROOMS.name + "/{buildingName}") { backStackEntry ->
                        RoomsScreen(
                            navController, backStackEntry.arguments?.getString("buildingName"),
                            roomViewModel
                        )
                    }
                    composable(NavRoute.BOOKING.name + "?roomId={roomId}") { backStackEntry ->
                        val roomId = backStackEntry.arguments?.getString("roomId")
                        BookingScreen(navController, roomId)
                    }
                    composable(NavRoute.SCHEDULE.name + "?roomNumber={roomNumber}&roomId={roomId}") { backStackEntry ->
                        val roomNumber = backStackEntry.arguments?.getString("roomNumber")
                        val roomId = backStackEntry.arguments?.getString("roomId")
                        RoomsSchedule(navController, roomNumber, roomId, roomViewModel)
                    }
                    composable(NavRoute.STATUS.name + "/{bookingId}") { backStackEntry ->
                        val bookingId = backStackEntry.arguments?.getString("bookingId")
                        StatusDetailScreen(navController, userViewModel, bookingId)
                    }
                    composable(NavRoute.STATUS.name) {
                        StatusesScreen(navController, userViewModel)
                    }
                    composable(NavRoute.EDIT_BOOKING.name + "/{bookingId}") { backStackEntry ->
                        val bookingId = backStackEntry.arguments?.getString("bookingId")
                        EditBookingScreen(navController, userViewModel, bookingId)
                    }
                    composable(NavRoute.SIGNUP.name) {
                        SignUpScreen(navController)
                    }
                }
            }
        }
    }
}

@HiltAndroidApp
class MainApplication : Application()