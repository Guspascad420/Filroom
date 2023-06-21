package com.example.filroom.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.filroom.model.response.Booking
import com.example.filroom.ui.theme.Green80
import com.example.filroom.ui.theme.Larsseit
import com.example.filroom.utils.NavRoute
import com.example.filroom.viewmodels.UserViewModel
import kotlinx.coroutines.delay
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusesScreen(navController: NavHostController, viewModel: UserViewModel) {
    val userProfileState = viewModel.userProfileState.collectAsState()
    val userBookingsState = viewModel.userBookingsState.collectAsState()

    LaunchedEffect(Unit) {
        if (userProfileState.value.data == null) {
            viewModel.getUserProfile()
        }
        delay(2000)
        userProfileState.value.data?.let {
            viewModel.getUserBookings(it.data.id)
        }
    }

    Scaffold(bottomBar = { BottomBar("Status", navController) }) {
        Surface(
            Modifier
                .fillMaxSize()
                .padding(it), color = Green80
        ) {
            Column(Modifier.padding(horizontal = 15.dp)) {
                Row(Modifier.padding(top = 15.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, Modifier.size(30.dp), Color.White)
                    }
                    Text(
                        "Status",
                        fontFamily = Larsseit,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
                Spacer(Modifier.height(15.dp))
                userBookingsState.value.data?.let { bookingsResponse ->
                    bookingsResponse.data.forEach { booking ->
                        StatusCard(booking = booking) {
                            navController.navigate(NavRoute.STATUS.name + "/${booking.id}")
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun StatusCard(booking: Booking, navigateToDetail: () -> Unit) {
    val dateTime = ZonedDateTime.parse(booking.date)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date = dateTime.format(formatter)

    Surface(
        Modifier
            .fillMaxWidth()
            .clickable { navigateToDetail() },
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                "${booking.room.building} - Ruang ${booking.room.number}", fontFamily = Larsseit,
                fontWeight = FontWeight.Bold, fontSize = 24.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                date, fontFamily = Larsseit,
                fontWeight = FontWeight.Medium, fontSize = 16.sp
            )
            Text(
                booking.time, fontFamily = Larsseit,
                fontWeight = FontWeight.Medium, fontSize = 16.sp
            )
        }
    }
}