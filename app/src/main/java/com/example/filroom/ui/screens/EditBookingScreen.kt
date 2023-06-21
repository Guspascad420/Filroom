package com.example.filroom.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.filroom.data.remote_source.Resource
import com.example.filroom.model.TimeRange
import com.example.filroom.model.toTimeRange
import com.example.filroom.ui.theme.Green80
import com.example.filroom.ui.theme.Larsseit
import com.example.filroom.ui.theme.Orange70
import com.example.filroom.utils.NavRoute
import com.example.filroom.viewmodels.BookingViewModel
import com.example.filroom.viewmodels.UserViewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookingScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    bookingId: String?
) {
    val bookingViewModel = hiltViewModel<BookingViewModel>()
    val userBookingsState = userViewModel.userBookingsState.collectAsState()
    val updateBookingsState = bookingViewModel.updateBookingsState.collectAsState()
    val userProfileState = userViewModel.userProfileState.collectAsState()
    val deleteBookingsState = bookingViewModel.deleteBookingsState.collectAsState()

    val timeRangeList = listOf(
        TimeRange("07.00", "08.39"),
        TimeRange("08.40", "10.19"),
        TimeRange("10.20", "11.59"),
        TimeRange("12.50", "14.29"),
        TimeRange("14.30", "16.19"),
        TimeRange("16.20", "18.00"),
    )
    val calendarState = rememberUseCaseState()
    var endStartTimeIndex = 0
    val allNotFilled = remember {
        derivedStateOf {
            bookingViewModel.name.isEmpty() || bookingViewModel.date.isEmpty() || bookingViewModel.startTime.isEmpty() ||
                    bookingViewModel.endTime.isEmpty() || bookingViewModel.requirement.isEmpty()
        }
    }

    LaunchedEffect(bookingViewModel.startTime) {
        if (bookingViewModel.startTime != "Waktu Mulai") {
            for (i in timeRangeList.indices) {
                if (bookingViewModel.startTime == timeRangeList[i].startTime) {
                    endStartTimeIndex = i
                    break
                }
            }
        }
    }

    LaunchedEffect(key1 = updateBookingsState.value) {
        when (updateBookingsState.value) {
            is Resource.Error -> {}
            is Resource.Loading -> {}
            is Resource.Success -> {
                navController.backQueue.clear()
                navController.navigate(NavRoute.STATUS.name)
            }
        }
    }

    LaunchedEffect(deleteBookingsState.value) {
        when (deleteBookingsState.value) {
            is Resource.Error -> {}
            is Resource.Loading -> {}
            is Resource.Success -> {
                navController.backQueue.clear()
                navController.navigate(NavRoute.STATUS.name)
            }
        }
    }

    LaunchedEffect(Unit) {
        userBookingsState.value.data?.let {
            val booking =
                it.data.filter { booking -> booking.id == bookingId.toString().toInt() }[0]
            val dateTime = ZonedDateTime.parse(booking.date)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = dateTime.format(formatter)

            bookingViewModel.name = booking.name
            bookingViewModel.date = date
            bookingViewModel.startTime = toTimeRange(booking.time).startTime
            bookingViewModel.endTime = toTimeRange(booking.time).endTime
            bookingViewModel.requirement = booking.requirement
            bookingViewModel.roomId = booking.room_id
        }
    }

    userProfileState.value.data?.let {
        bookingViewModel.userId = it.data.id
    }

    Surface(Modifier.fillMaxSize(), color = Green80) {
        CalendarDialog(state = calendarState, selection = CalendarSelection.Date { date ->
            bookingViewModel.date = date.toString()
        })
        Column(Modifier.padding(horizontal = 10.dp)) {
            Row(Modifier.padding(top = 15.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null, Modifier.size(30.dp), Color.White)
                }
                Text(
                    "Data Peminjaman",
                    fontFamily = Larsseit,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(25.dp))
            Column(Modifier.padding(horizontal = 15.dp)) {
                Text(
                    "Nama Peminjam", fontFamily = Larsseit,
                    fontWeight = FontWeight.Medium, fontSize = 20.sp,
                    color = Color.White
                )
                TextField(
                    bookingViewModel.name,
                    placeholder = {
                        Text(
                            "Nama",
                            fontFamily = Larsseit,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    onValueChange = { bookingViewModel.name = it },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Green80,
                        unfocusedIndicatorColor = Color.White
                    ),
                    textStyle = TextStyle(
                        color = Color.White,
                        fontFamily = Larsseit,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                )
                Spacer(Modifier.height(23.dp))
                Text(
                    "Tanggal", fontFamily = Larsseit,
                    fontWeight = FontWeight.Medium, fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = { calendarState.show() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green80,
                    ),
                    shape = RoundedCornerShape(15.dp),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Text(
                        bookingViewModel.date, fontFamily = Larsseit,
                        fontWeight = FontWeight.Medium, fontSize = 18.sp,
                        color = Color.White
                    )
                    Spacer(Modifier.width(20.dp))
                    Icon(Icons.Default.CalendarMonth, "calendar", tint = Color.White)
                }
                Spacer(Modifier.height(15.dp))
                Text(
                    "Waktu Peminjaman", fontFamily = Larsseit,
                    fontWeight = FontWeight.Medium, fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(20.dp))
                ExposedDropdownMenuBox(
                    expanded = bookingViewModel.startTimeExpanded,
                    onExpandedChange = {
                        bookingViewModel.startTimeExpanded = !bookingViewModel.startTimeExpanded
                    },
                ) {
                    OutlinedTextField(
                        value = bookingViewModel.startTime,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                null,
                                Modifier.rotate(if (bookingViewModel.startTimeExpanded) 180f else 0f),
                                Color.White
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .border(BorderStroke(3.dp, Color.White), RoundedCornerShape(15.dp))
                            .height(60.dp)
                            .width(200.dp),
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
                        expanded = bookingViewModel.startTimeExpanded,
                        onDismissRequest = { bookingViewModel.startTimeExpanded = false }
                    ) {
                        for (timeRange in timeRangeList) {
                            DropdownMenuItem(
                                text = { Text(timeRange.startTime) },
                                onClick = {
                                    bookingViewModel.startTime = timeRange.startTime
                                    bookingViewModel.startTimeExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(15.dp))
                ExposedDropdownMenuBox(
                    expanded = bookingViewModel.endTimeExpanded,
                    onExpandedChange = {
                        bookingViewModel.endTimeExpanded = !bookingViewModel.endTimeExpanded
                    },
                ) {
                    OutlinedTextField(
                        value = bookingViewModel.endTime,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                null,
                                Modifier.rotate(if (bookingViewModel.endTimeExpanded) 180f else 0f),
                                Color.White
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .border(BorderStroke(3.dp, Color.White), RoundedCornerShape(15.dp))
                            .height(60.dp)
                            .width(200.dp),
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
                        expanded = bookingViewModel.endTimeExpanded,
                        onDismissRequest = { bookingViewModel.endTimeExpanded = false }
                    ) {
                        for (i in endStartTimeIndex until timeRangeList.size) {
                            DropdownMenuItem(
                                text = { Text(timeRangeList[i].endTime) },
                                onClick = {
                                    bookingViewModel.endTime = timeRangeList[i].endTime
                                    bookingViewModel.endTimeExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(15.dp))
                Text(
                    "Keperluan", fontFamily = Larsseit,
                    fontWeight = FontWeight.Medium, fontSize = 20.sp,
                    color = Color.White
                )
                TextField(
                    bookingViewModel.requirement,
                    placeholder = {
                        Text(
                            "Keperluan",
                            fontFamily = Larsseit,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    onValueChange = { bookingViewModel.requirement = it },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Green80,
                        unfocusedIndicatorColor = Color.White
                    ),
                    textStyle = TextStyle(
                        color = Color.White,
                        fontFamily = Larsseit,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                )
                Spacer(Modifier.height(30.dp))
                Button(
                    onClick = {
                        if (!allNotFilled.value) {
                            bookingViewModel.updateBookings(bookingId.toString())
                        }
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = Orange70
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(47.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Edit", fontFamily = Larsseit,
                        fontWeight = FontWeight.Bold, fontSize = 16.sp,
                        color = Color.White
                    )
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { bookingViewModel.openDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(47.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Delete", fontFamily = Larsseit,
                        fontWeight = FontWeight.Bold, fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
            if (bookingViewModel.openDialog) {
                ConfirmDeleteDialog(bookingViewModel, bookingId.toString())
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(bookingViewModel: BookingViewModel, bookingId: String) {
    Dialog(onDismissRequest = { bookingViewModel.openDialog = false }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            color = Color.Black,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                Modifier
                    .padding(25.dp, 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Apakah kamu yakin ingin menghapus?", fontFamily = Larsseit,
                    fontWeight = FontWeight.Bold, fontSize = 22.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(18.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Button(
                        onClick = { bookingViewModel.openDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Orange70
                        )
                    ) {
                        Text(
                            "Batal", fontFamily = Larsseit,
                            fontWeight = FontWeight.Bold, fontSize = 17.sp,
                            color = Color.White
                        )
                    }
                    Button(
                        onClick = {
                            bookingViewModel.deleteBookings(bookingId)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        ),
                    ) {
                        Text(
                            "Hapus", fontFamily = Larsseit,
                            fontWeight = FontWeight.Bold, fontSize = 17.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}