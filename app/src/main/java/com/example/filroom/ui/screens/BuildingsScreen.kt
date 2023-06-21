package com.example.filroom.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.filroom.R
import com.example.filroom.ui.theme.Green50
import com.example.filroom.ui.theme.Green80
import com.example.filroom.ui.theme.Larsseit
import com.example.filroom.ui.theme.Orange70
import com.example.filroom.utils.NavRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildingsScreen(navController: NavHostController) {
    val buildings = listOf("Auditorium", "Gedung F", "GKM", "Lapangan\nBasket")

    Scaffold(bottomBar = { BottomBar("Home", navController) }) {
        Surface(
            Modifier
                .fillMaxSize()
                .padding(it), color = Green80) {
            Column(Modifier.padding(horizontal = 30.dp)) {
                Spacer(Modifier.height(40.dp))
                Text(
                    "Selamat Datang", fontFamily = Larsseit,
                    fontWeight = FontWeight.Bold, color = Color.White,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(45.dp))
                buildings.forEach { building ->
                    Box {
                        Surface(
                            Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clickable { navController.navigate(NavRoute.ROOMS.name + "/" + building) },
                            color = Orange70,
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Row(
                                Modifier
                                    .padding(start = 15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.LocationOn, null, Modifier.size(30.dp), Color.White)
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    building, fontFamily = Larsseit,
                                    fontWeight = FontWeight.Bold, fontSize = 24.sp,
                                    color = Color.White
                                )
                            }
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .offset(x = 20.dp, y = (-50).dp), Arrangement.End) {
                            Image(painterResource(R.drawable.polos_3), null)
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun BottomBar(active: String, navController: NavHostController) {
    val homeTint = if (active == "Home") Green50 else Color(0xFF767474)
    val statusTint = if (active == "Status") Green50 else Color(0xFF767474)
    BottomAppBar(containerColor = Color.White) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = {
                    navController.backQueue.clear()
                    navController.navigate(NavRoute.BUILDINGS.name)
                }) {
                    Icon(painterResource(R.drawable.home), null, tint = homeTint)
                }
                Text("Home", fontFamily = Larsseit, fontSize = 12.sp, color = homeTint)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { navController.navigate(NavRoute.STATUS.name) }) {
                    Icon(painterResource(R.drawable.status), null, tint = statusTint)
                }
                Text("Status", fontFamily = Larsseit, fontSize = 12.sp, color = homeTint)
            }
        }
    }
}