package com.example.rationmanproject

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SubsidyCheckScreen(navController: NavController) {

    val viewModel : locationviewmodel = hiltViewModel()
    val subsidy by viewModel.subsidyResponse.collectAsState()
    val context = LocalContext.current
    var cardopening by remember {
        mutableStateOf(false)
    }
    var hasChecked by remember { mutableStateOf(false) }



    var aadharnumber by remember{ mutableStateOf("") }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1b5e20), Color(0xFF81c784)),
                    endY = 250.0f
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.screenhome),
            contentDescription = "",
            Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xCC000000))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.navigate(Screen.homescreen.route) }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")

                    }
                }
                Spacer(modifier = Modifier.height(100.dp))

                Text(
                    text = "AADHAAR NUMBER",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.White   // <-- LIGHT TEXT
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = aadharnumber,
                    onValueChange = { aadharnumber = it },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.Black,
                        disabledTextColor = Color.Gray,

                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray,
                        disabledIndicatorColor = Color.DarkGray,

                        cursorColor = Color.White,

                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,

                        focusedPlaceholderColor = Color.LightGray,
                        unfocusedPlaceholderColor = Color.LightGray,
                    ),
                    placeholder = {
                        Text("Enter Aadhaar")
                    }
                )


                Spacer(modifier = Modifier.height(15.dp))

                Button(onClick = {

                    if (aadharnumber.length != 12) {
                        Toast.makeText(context, "Enter valid 12-digit Aadhaar", Toast.LENGTH_SHORT)
                            .show()
                    } else {

                        viewModel.checkSubsidy(aadharnumber)
                        hasChecked = true
                    }
                }) {
                    Text(text = "Check")

                }
            }

            if (cardopening) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedVisibility(
                        visible = cardopening,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {

                        subsidy?.let {
                            CheckSubsidyCard(it, aadharnumber) {
                                cardopening = false
                                aadharnumber = ""
                            }
                        }

                    }
                }
            }

            LaunchedEffect(subsidy) {
                if (hasChecked) {
                    if (subsidy == null) {
                        Toast.makeText(
                            context,
                            "No subsidy information available",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        cardopening = true
                    }
                }
            }
        }
    }


}


@Composable
fun CheckSubsidyCard(
    subsidy: subsidyapiresponse,
    aadhar: String,
    onBack: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF69F0AE) // very light green
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            // Top row with back arrow + Aadhaar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { onBack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = subsidy.aadhaar,
                    style = TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Card contents
            InfoRow(label = "Scheme", value = subsidy.scheme)
            InfoRow(label = "Remaining", value = subsidy.remaining)
            InfoRow(label = "Entitlements", value = subsidy.entitlements)
            InfoRow(label = "Monthly Limit", value = subsidy.monthlyLimit)
            InfoRow(label = "Valid Until", value = subsidy.validUntil)
        }
    }
}


@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = "$label :",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
