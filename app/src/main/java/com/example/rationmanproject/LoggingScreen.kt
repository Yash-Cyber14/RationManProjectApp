package com.example.rationmanproject

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun Loggingscreen(navController: NavController ){

    val viewModel : locationviewmodel = hiltViewModel()
    val afterloggingscreen = viewModel.afterloggingscreen.collectAsState()

    Box(modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center){
        CircularProgressIndicator()

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp) , contentAlignment = Alignment.BottomEnd){
            Text(text = "LOGGING IN ...." , style = TextStyle(fontWeight = FontWeight.SemiBold , fontStyle = FontStyle.Italic , fontSize = 15.sp))

        }

    }

    LaunchedEffect(Unit){
        viewModel.loggingstatus()
    }

    LaunchedEffect(afterloggingscreen.value) {
        afterloggingscreen.value?.let { screen ->
            navController.navigate(screen.route) {
                // optional: clears backstack so you can’t go back to logging screen
            }
        }
    }







}


