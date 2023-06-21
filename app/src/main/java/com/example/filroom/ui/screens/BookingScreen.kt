package com.example.filroom.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.filroom.data.remote_source.Resource
import com.example.filroom.model.TimeRange
import com.example.filroom.ui.theme.Green80
import com.example.filroom.ui.theme.Larsseit
import com.example.filroom.ui.theme.Orange70
import com.example.filroom.utils.NavRoute
import com.example.filroom.viewmodels.BookingViewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(navController: NavHostController, roomId: String?) {
    val viewModel = hiltViewModel<BookingViewModel>()
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
            viewModel.name.isEmpty() || viewModel.date.isEmpty() || viewModel.startTime.isEmpty() ||
                    viewModel.endTime.isEmpty() || viewModel.requirement.isEmpty()
        }
    }
    val createBookingsState = viewModel.createBookingsState.collectAsState()
    val userProfileState = viewModel.userProfileState.collectAsState()
    if (roomId != null) {
        viewModel.roomId = roomId.toInt()
    }

    LaunchedEffect(key1 = userProfileState) {
        viewModel.getUserProfile()
    }
    userProfileState.value.data?.let {
        viewModel.userId = it.data.id
        Log.d("TAG", viewModel.userId.toString())
    }

    LaunchedEffect(viewModel.startTime) {
        if (viewModel.startTime != "Waktu Mulai") {
            for (i in timeRangeList.indices) {
                if (viewModel.startTime == timeRangeList[i].startTime) {
                    endStartTimeIndex = i
                    break
                }
            }
        }
    }

    LaunchedEffect(key1 = createBookingsState.value) {
        when (createBookingsState.value) {
            is Resource.Error -> {}
            is Resource.Loading -> {}
            is Resource.Success -> {
                navController.backQueue.clear()
                navController.navigate(NavRoute.STATUS.name)
            }
        }
    }


    Surface(Modifier.fillMaxSize(), color = Green80) {
        CalendarDialog(state = calendarState, selection = CalendarSelection.Date { date ->
            viewModel.date = date.toString()
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
                    viewModel.name,
                    placeholder = {
                        Text(
                            "Nama",
                            fontFamily = Larsseit,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    onValueChange = { viewModel.name = it },
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
                        viewModel.date, fontFamily = Larsseit,
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
                    expanded = viewModel.startTimeExpanded,
                    onExpandedChange = {
                        viewModel.startTimeExpanded = !viewModel.startTimeExpanded
                    },
                ) {
                    OutlinedTextField(
                        value = viewModel.startTime,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                null,
                                Modifier.rotate(if (viewModel.startTimeExpanded) 180f else 0f),
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
                        expanded = viewModel.startTimeExpanded,
                        onDismissRequest = { viewModel.startTimeExpanded = false }
                    ) {
                        for (timeRange in timeRangeList) {
                            DropdownMenuItem(
                                text = { Text(timeRange.startTime) },
                                onClick = {
                                    viewModel.startTime = timeRange.startTime
                                    viewModel.startTimeExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(15.dp))
                ExposedDropdownMenuBox(
                    expanded = viewModel.endTimeExpanded,
                    onExpandedChange = { viewModel.endTimeExpanded = !viewModel.endTimeExpanded },
                ) {
                    OutlinedTextField(
                        value = viewModel.endTime,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                null,
                                Modifier.rotate(if (viewModel.endTimeExpanded) 180f else 0f),
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
                        expanded = viewModel.endTimeExpanded,
                        onDismissRequest = { viewModel.endTimeExpanded = false }
                    ) {
                        for (i in endStartTimeIndex until timeRangeList.size) {
                            DropdownMenuItem(
                                text = { Text(timeRangeList[i].endTime) },
                                onClick = {
                                    viewModel.endTime = timeRangeList[i].endTime
                                    viewModel.endTimeExpanded = false
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
                    viewModel.requirement,
                    placeholder = {
                        Text(
                            "Keperluan",
                            fontFamily = Larsseit,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    onValueChange = { viewModel.requirement = it },
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
                            viewModel.createBookings()
                        } else {
                            Log.d("TAG", "dioqduifn")
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
                        "Kirim", fontFamily = Larsseit,
                        fontWeight = FontWeight.Bold, fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}