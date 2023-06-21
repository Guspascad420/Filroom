package com.example.filroom.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.filroom.R
import com.example.filroom.data.remote_source.Resource
import com.example.filroom.ui.theme.Green50
import com.example.filroom.ui.theme.Green80
import com.example.filroom.ui.theme.Larsseit
import com.example.filroom.ui.theme.Orange70
import com.example.filroom.utils.NavRoute
import com.example.filroom.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val viewModel = hiltViewModel<AuthViewModel>()
    val visualTransformation = if (viewModel.isPasswordVisible) VisualTransformation.None
    else PasswordVisualTransformation()
    val loginState = viewModel.loginState.collectAsState()
    val allNotFilled = remember {
        derivedStateOf {
            viewModel.email.isEmpty() || viewModel.password.isEmpty()
        }
    }

    LaunchedEffect(key1 = loginState.value) {
        when (loginState.value) {
            is Resource.Error -> {}
            is Resource.Loading -> {}
            is Resource.Success -> {
                loginState.value.data?.let {
                    viewModel.saveToken(it.data.token)
                    delay(2000)
                    navController.backQueue.clear()
                    navController.navigate(NavRoute.BUILDINGS.name)
                }
            }
        }
    }

    Column(Modifier.padding(horizontal = 20.dp)) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            Image(
                painterResource(R.drawable.filroom_logo), null,
                Modifier
                    .padding(vertical = 75.dp)
                    .size(180.dp)
            )
        }
        if (viewModel.error) {
            Text(
                "Tolong masukkan data dengan benar", fontFamily = Larsseit,
               color = Color.Red, fontSize = 17.sp
            )
        }
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Email") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Green50
            )
        )
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Password") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Green50
            ),
            visualTransformation = visualTransformation,
            trailingIcon = {
                IconButton(
                    onClick = {
                        viewModel.isPasswordVisible = !viewModel.isPasswordVisible
                    }) {
                    Icon(
                        imageVector = if (viewModel.isPasswordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        null
                    )
                }
            }
        )
        Spacer(Modifier.height(40.dp))
        Button(
            onClick = {
                if (!allNotFilled.value) {
                    viewModel.login()
                } else {
                    viewModel.error = true
                }
//                navController.navigate(NavRoute.BUILDINGS.name)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Green50
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Masuk", Modifier.padding(vertical = 8.dp), fontFamily = Larsseit,
                fontWeight = FontWeight.Bold, color = Color.White, fontSize = 17.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(vertical = 25.dp)
                .fillMaxWidth()
        ) {
            Divider(
                Modifier
                    .width(100.dp)
                    .padding(end = 10.dp), color = Green50
            )
            Text("Atau")
            Divider(
                Modifier
                    .width(100.dp)
                    .padding(start = 10.dp), color = Green50
            )
        }
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange70
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Masuk dengan Google",
                Modifier.padding(vertical = 8.dp),
                fontFamily = Larsseit,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                fontSize = 17.sp
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Belum memiliki akun? ",
                fontFamily = Larsseit,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp,
            )
            ClickableText(
                text = AnnotatedString("Daftar"),
                style = TextStyle(
                    textDecoration = TextDecoration.Underline,
                    fontFamily = Larsseit,
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    color = Green50
                ),
                onClick = { navController.navigate(NavRoute.SIGNUP.name) }
            )
        }
    }
}