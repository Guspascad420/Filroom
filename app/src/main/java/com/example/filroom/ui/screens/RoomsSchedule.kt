package com.example.filroom.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.filroom.model.TimeRange
import com.example.filroom.model.response.Booking
import com.example.filroom.ui.library.JetLimeView
import com.example.filroom.ui.theme.Green80
import com.example.filroom.ui.theme.Larsseit
import com.example.filroom.ui.theme.Orange70
import com.example.filroom.utils.NavRoute
import com.example.filroom.viewmodels.RoomViewModel
import com.pushpal.jetlime.data.JetLimeItem
import com.pushpal.jetlime.data.config.JetLimeItemConfig
import com.pushpal.jetlime.data.config.JetLimeViewConfig
import com.pushpal.jetlime.data.config.LineType

@Composable
fun RoomsSchedule(
    navController: NavHostController, roomNumber: String?,
    roomId: String?, viewModel: RoomViewModel
) {
    val bookingsState = viewModel.bookingsState.collectAsState()
    var bookings: List<Booking> = listOf()

    val timeRangeList = listOf(
        TimeRange("07.00", "08.39"),
        TimeRange("08.40", "10.19"),
        TimeRange("10.20", "11.59"),
        TimeRange("12.50", "14.29"),
        TimeRange("14.30", "16.19"),
        TimeRange("16.20", "18.00"),
    )
    bookingsState.value.data?.let {
        bookings = it.data.filter { booking -> booking.room_id.toString() == roomId.toString() }
    }

    val jetLimeList = mutableListOf<JetLimeItem>()
    for (timeRange in timeRangeList) {
        jetLimeList.add(
            JetLimeItem(
                title = "",
                jetLimeItemConfig = JetLimeItemConfig(
                    titleColor = Color.White,
                    descriptionColor = Color.White,
                    itemHeight = 90.dp
                )
            ) {
                Column(Modifier.padding(top = 8.dp)) {
                    Text(
                        timeRange.toString(), fontFamily = Larsseit,
                        fontWeight = FontWeight.Medium, color = Color.White,
                        fontSize = 18.sp
                    )
                    Text("Tersedia", fontFamily = Larsseit, color = Color.White)
                }
            }
        )
    }

    Surface(Modifier.fillMaxSize(), color = Green80) {
        Column(Modifier.padding(horizontal = 10.dp)) {
            Row(Modifier.padding(top = 15.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null, Modifier.size(30.dp), Color.White)
                }
                Text(
                    "Ruang ${roomNumber.toString().uppercase()}",
                    fontFamily = Larsseit,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(20.dp))
            JetLimeView(
                jetLimeItems = jetLimeList,
                jetLimeViewConfig = JetLimeViewConfig(
                    lineType = LineType.Solid,
                    iconBorderThickness = 5.dp,
                    backgroundColor = Color.Unspecified,
                    lineColor = Color.White,
                    iconSize = 17.dp,
                )
            )
            Button(
                onClick = { navController.navigate(NavRoute.BOOKING.name + "?roomId=$roomId") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange70
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Pinjam Ruangan", fontFamily = Larsseit,
                    fontWeight = FontWeight.Bold, color = Color.White,
                    fontSize = 17.sp
                )
            }
        }
    }
}