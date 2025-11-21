package com.example.rationmanproject

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun SignIn(navcontroller: NavHostController ) {

    val viewModel: AuthViewModel = viewModel()
    val Context : Context = LocalContext.current
    val coroutinescope = rememberCoroutineScope()
    val signupstate =viewModel.signupstate.value

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF81c784)) ){

        Canvas(modifier = Modifier.fillMaxSize())
        {
            val path = Path().apply {
                val width : Float = size.width
                moveTo(-180f,0f)
                quadraticBezierTo(width/2,500f,width+180,0f)

            }


            drawPath(
                path=path,
                color = Color(0xFF1b5e20),
                style = Fill
            )
            drawPath(
                path=path,
                color = Color.White,
                style = Stroke(width = 15f)
            )
            drawCircle(
                color = Color.White,
                radius = 15f,
                center = Offset(size.width/2,210f)

            )
            drawCircle(
                color = Color.White,
                radius = 15f,
                center = Offset(size.width/2,160f)

            )
            drawCircle(
                color = Color.White,
                radius = 15f,
                center = Offset((size.width/2)+25,185f)

            )
            drawCircle(
                color = Color.White,
                radius = 15f,
                center = Offset((size.width/2)-25,185f)

            )

        }


        var email = remember { mutableStateOf("") }
        var password = remember { mutableStateOf("") }
        val context = LocalContext.current



        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp)
        ) {
            Column(modifier= Modifier
                .fillMaxSize()
                .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Sign In", style= TextStyle(
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold
                ))

                Spacer(modifier = Modifier.height(70.dp))

                OutlinedTextField(value = email.value
                    , onValueChange = { email.value = it},
                    label = { Text(text = "Enter email")},
                    singleLine = true,
                    shape = RoundedCornerShape(40.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(value = password.value
                    , onValueChange = { password.value = it},
                    label = { Text(text = "Enter password")},
                    singleLine = true,
                    shape = RoundedCornerShape(40.dp),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(70.dp))

//            LaunchedEffect(key1 = viewModel.signupstate.value.confirm){
//
//
//                if(viewModel.signupstate.value.confirm == true)
//                {
//                    Toast.makeText(Context,"Successfully Signed In",Toast.LENGTH_LONG).show()
//                    // signing in and navigation
//
//                }
//
//
//
//            }
//
//            LaunchedEffect(key1 = viewModel.signupstate.value.confirm){
//
//                if(viewModel.signupstate.value.confirm == true){
//
//                }
//            }
//
//            LaunchedEffect(key1 = viewModel.signupstate.value.errormessage){
//
//                if(viewModel.signupstate.value.errormessage != null){
//                    Toast.makeText(Context,viewModel.signupstate.value.errormessage , Toast.LENGTH_LONG).show()
//                }
//
//            }

                Button(onClick = {
                    viewModel.signin(email.value , password.value)

                    if(viewModel.signupstate.value.confirm == true)
                    {
                        Toast.makeText(context,"Successfully Signed In",Toast.LENGTH_LONG).show()
                        // signing in and navigation
                        navcontroller.navigate(Screen.homescreen.route)

                    }

                    if(viewModel.signupstate.value.errormessage != null){
                        Toast.makeText(context,viewModel.signupstate.value.errormessage , Toast.LENGTH_LONG).show()
                    }




                }) {
                    Text(text = "Sign In",style=MaterialTheme.typography.headlineSmall)


                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Text(text = "If you dont have an account", style = MaterialTheme.typography.bodySmall)
                    Text(text = "  Sign Up" , modifier = Modifier.clickable {
                        navcontroller.navigate(
                            Screen.Signup.route)
                    },
                        style=MaterialTheme.typography.bodySmall)
                }



            }

        }

    }






}