package com.example.rationmanproject

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import javax.inject.Inject

@Composable
fun ProfileScreen(navController: NavController, viewModel: AuthViewModel) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    var actionCardState by rememberSaveable { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        // 🌄 Background image
        Image(
            painter = painterResource(R.drawable.screenhome),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 🖤 Semi-transparent overlay + beige shape + content
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xCC000000))
        ) {
            // 🎨 Curved beige background shape
            Canvas(Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                val path = Path().apply {
                    moveTo(0f, height)
                    lineTo(0f, height / 3)
                    quadraticTo(width / 2, (height / 2.2).toFloat(), width, height / 3)
                    lineTo(width, height)
                    close()
                }
                drawPath(path = path, color = Color(0xFFFAF0E6), style = Fill)
            }

            // 🌟 Main centered content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(Modifier.height(150.dp))

                // 🧑‍💼 Profile Title
                Text(
                    text = "Profile",
                    style = TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic
                    ),
                    color = Color.White,
                    fontSize = 56.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(40.dp))

                // 👤 Profile Info Fields
                GeneralInfo(viewModel)

                Spacer(Modifier.height(40.dp))

                // 🚪 Sign Out Button
                Button(
                    onClick = {
                        viewModel.signOut(
                            context = context
                        )
                        navController.navigate(Screen.Signup.route) {
                            popUpTo(0)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = "Sign Out",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(Modifier.height(100.dp))
            }
        }

        // 🔙 Back button (TOPMOST, draws last)
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color(0x66000000), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}




@Composable
fun GeneralInfo(viewModel: AuthViewModel) {

    val email = viewModel.profileemail.value

    Column(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth(0.9f)
    ) {

        OutlinedTextField(
            value = email,
            onValueChange = {},
            readOnly = true,
            label = { Text("Email") }
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = "N/A",
            onValueChange = {},
            readOnly = true,
            label = { Text("UserId") }
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = "N/A",
            onValueChange = {},
            readOnly = true,
            label = { Text("Display Name") }
        )
    }
}