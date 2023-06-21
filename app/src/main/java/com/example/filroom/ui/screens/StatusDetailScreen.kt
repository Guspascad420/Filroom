package com.example.filroom.ui.screens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.full.memberProperties

@Composable
fun StatusDetailScreen(navController: NavHostController, viewModel: UserViewModel, bookingId: String?) {
    val userBookingsState = viewModel.userBookingsState.collectAsState()

    Surface(Modifier.fillMaxSize(), color = Green80) {
        Column(Modifier.padding(start = 10.dp)) {
            Row(Modifier.padding(top = 15.dp).fillMaxWidth(), SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.navigate(NavRoute.BUILDINGS.name) }) {
                        Icon(Icons.Default.ArrowBack, null, Modifier.size(30.dp), Color.White)
                    }
                    Text(
                        "Detail Status",
                        fontFamily = Larsseit,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        color = Color.White
                    )
                }
                IconButton(onClick = { navController.navigate(NavRoute.EDIT_BOOKING.name + "/" + bookingId.toString()) }) {
                    Icon(Icons.Default.Edit, null, Modifier.size(30.dp), Color.White)
                }
            }
            Spacer(Modifier.height(45.dp))
            Column(Modifier.padding(start = 15.dp)) {
                userBookingsState.value.data?.let {
                    val booking = it.data.filter { booking -> booking.id == bookingId.toString().toInt() }[0]
                    val dateTime = ZonedDateTime.parse(booking.date)
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val date = dateTime.format(formatter)

                    Log.d("TAG", bookingId.toString())

                    BookingAttr("Nama", booking.name)
                    BookingAttr("Tanggal Peminjaman", date)
                    BookingAttr("Ruang", "${booking.room.building} - ${booking.room.number}")
                    BookingAttr("Waktu Peminjaman", booking.time)
                    BookingAttr("Keperluan", booking.requirement)
                    BookingAttr("Status", "Diterima")
                }
            }
        }
    }
}

@Composable
fun BookingAttr(attribute: String, value: String) {
    Row(Modifier.padding(bottom = 15.dp)) {
        Canvas(modifier = Modifier
            .size(8.dp)
            .offset(y = 8.dp), onDraw = {
            drawCircle(color = Color.White)
        })
        Spacer(Modifier.width(20.dp))
        Column {
            Text(attribute, fontFamily = Larsseit, fontSize = 18.sp, color = Color.White)
            Spacer(Modifier.height(8.dp))
            Text(
                value, fontFamily = Larsseit,
                fontSize = 24.sp, color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}