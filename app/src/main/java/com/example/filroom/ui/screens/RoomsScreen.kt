package com.example.filroom.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.filroom.R
import com.example.filroom.model.TimeRange
import com.example.filroom.model.checkDateRange
import com.example.filroom.model.response.Booking
import com.example.filroom.model.response.Room
import com.example.filroom.model.toTimeRange
import com.example.filroom.ui.theme.Green80
import com.example.filroom.ui.theme.Larsseit
import com.example.filroom.ui.theme.Orange70
import com.example.filroom.utils.NavRoute
import com.example.filroom.viewmodels.RoomViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsScreen(
    navController: NavHostController,
    buildingName: String?,
    viewModel: RoomViewModel
) {
    val roomsState = viewModel.roomsState.collectAsState()
    val bookingsState = viewModel.bookingsState.collectAsState()
    val url = buildingName.toString().replace(Regex("[\\s\n]"), "-")

    val date = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formatted = date.format(formatter)

    var bookings: List<Booking> = listOf()

    LaunchedEffect(roomsState, bookingsState) {
        viewModel.getAllRooms(url)
        viewModel.getAllBookings(formatted)
    }

    bookingsState.value.data?.let {
        bookings = it.data.filter { booking -> booking.room.building == buildingName.toString() }
    }

    Surface(Modifier.fillMaxSize(), color = Green80) {
        Column(Modifier.padding(horizontal = 10.dp)) {
            Row(Modifier.padding(top = 15.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null, Modifier.size(30.dp), Color.White)
                }
                Text(
                    buildingName.toString().uppercase(),
                    fontFamily = Larsseit,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            if (buildingName.toString() == "Gedung F") {
                ExposedDropdownMenuBox(
                    expanded = viewModel.expanded,
                    onExpandedChange = { viewModel.expanded = !viewModel.expanded },
                    Modifier.padding(vertical = 25.dp, horizontal = 10.dp)
                ) {
                    OutlinedTextField(
                        value = "Lantai ${viewModel.selectedFloor}",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                null,
                                Modifier.rotate(if (viewModel.expanded) 180f else 0f),
                                Color.White
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .border(BorderStroke(3.dp, Color.White), RoundedCornerShape(15.dp)),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Green80,
                            unfocusedBorderColor = Color.White
                        ),
                        shape = RoundedCornerShape(15.dp),
                        textStyle = TextStyle(
                            fontFamily = Larsseit,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = viewModel.expanded,
                        onDismissRequest = { viewModel.expanded = false }
                    ) {
                        for (i in 2..5) {
                            DropdownMenuItem(
                                text = { Text("Lantai $i") },
                                onClick = {
                                    viewModel.selectedFloor = i
                                    viewModel.expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.padding(10.dp))
            } else {
                Spacer(Modifier.height(25.dp))
            }
            roomsState.value.data?.let {
                it.data.forEach { room ->
                    var timeRange = TimeRange("", "")
                    val roomBookings = bookings.filter { booking ->
                        timeRange = toTimeRange(booking.time)
                        booking.room == room && checkDateRange(timeRange)
                    }
                    if (roomBookings.isNotEmpty()) {
                        UnavailableRoomCard(room, timeRange.endTime)
                    } else {
                        RoomCard(navController, room)
                    }
                }
            }
        }
    }
}

@Composable
fun RoomCard(navController: NavHostController, room: Room) {
    val number =
        if (room.number % 1 == 0F) room.number.toInt().toString() else room.number
    Box(Modifier.padding(horizontal = 20.dp)) {
        Surface(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable {
                    navController.navigate(NavRoute.SCHEDULE.name + "?roomNumber=${room.number}&roomId=${room.id}")
                },
            color = Orange70,
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(Modifier.padding(start = 15.dp), Arrangement.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        null,
                        Modifier.size(30.dp),
                        Color.White
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        "Ruang $number", fontFamily = Larsseit,
                        fontWeight = FontWeight.Bold, fontSize = 24.sp,
                        color = Color.White
                    )
                }
                Text(
                    "Tersedia", Modifier.padding(start = 35.dp),
                    fontFamily = Larsseit, color = Color.White
                )
            }

        }
        Row(
            Modifier
                .fillMaxWidth()
                .offset(x = 20.dp, y = (-50).dp), Arrangement.End
        ) {
            Image(painterResource(R.drawable.polos_3), null)
        }
    }
}

@Composable
fun UnavailableRoomCard(room: Room, endTime: String) {
    val matrix = ColorMatrix()
    matrix.setToSaturation(0F)
    val number =
        if (room.number % 1 == 0F) room.number.toInt().toString() else room.number
    Box(Modifier.padding(horizontal = 20.dp)) {
        Surface(
            Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(15.dp),
            color = Color(0xFF767474)
        ) {
            Column(Modifier.padding(start = 15.dp), Arrangement.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        null,
                        Modifier.size(30.dp),
                        Color(0xFFC3C3C3)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        "Ruang $number", fontFamily = Larsseit,
                        fontWeight = FontWeight.Bold, fontSize = 24.sp,
                        color = Color(0xFFC3C3C3)
                    )
                }
                Text(
                    "Digunakan hingga pukul $endTime",
                    Modifier
                        .padding(start = 35.dp)
                        .width(180.dp),
                    fontFamily = Larsseit,
                    color = Color(0xFFC3C3C3)
                )
            }

        }
        Row(
            Modifier
                .fillMaxWidth()
                .offset(x = 20.dp, y = (-50).dp), Arrangement.End
        ) {
            Image(
                painterResource(R.drawable.polos_3), null,
                colorFilter = ColorFilter.colorMatrix(matrix)
            )
        }
    }
}