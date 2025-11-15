package com.example.rationmanproject

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import com.google.firebase.Timestamp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(userloc : userlocation?, navController: NavController){

    var searchstock by remember {mutableStateOf("")}
    val viewModel : locationviewmodel = hiltViewModel()
    var openreportcard by remember {mutableStateOf(false)}
    val context = LocalContext.current
    val shops by viewModel.allshops.collectAsState()
    var selectedreportshop by remember { mutableStateOf <Pair<apiresponse , Float>?>(null) }

    var id by remember{ mutableStateOf("") }

    LaunchedEffect(userloc){
        if (userloc != null) {

            viewModel.getallshops(userloc)
        }
    }



    LaunchedEffect(Unit){
        viewModel.uievents.collect{message ->
            Toast.makeText(context , message , Toast.LENGTH_SHORT).show()

        }

    }








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
                    .padding(8.dp)
            ) {

                IconButton(onClick = { navController.navigate(Screen.homescreen.route) }) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back" , tint = Color.White)


                }
                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = searchstock,
                    onValueChange = {
                        searchstock = it
                        if (userloc != null) {

                            viewModel.filtershopsreportscreen(userloc, searchstock)
                        }
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search")
                    },
                    placeholder = { Text("Search...") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0x8027AE60),
                        unfocusedContainerColor = Color(0x8027AE60),
                        disabledContainerColor = Color(0x8027AE60),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))







                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .background(color = Color(0x8027AE60))
                ) {
                    LazyColumn(
                        Modifier.padding(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(shops) { shop ->
                            shoplazycolumn(shop = shop) {
                                selectedreportshop = shop
                                openreportcard = true
                                id = shop.first.id


                            }

                        }
                    }

                }


            }
            AnimatedVisibility(
                visible = openreportcard,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()

            ) {
                selectedreportshop?.let {
                    reportcard(selectedreportshop!!) {
                        openreportcard = false
                    }
                }

            }


        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun reportcard(
    shop: Pair<apiresponse, Float>,
    onDismiss: () -> Unit = {}
) {
    var show by remember { mutableStateOf(false) }
    val viewModel: locationviewmodel = hiltViewModel()
    val id = shop.first.id
    var report by remember { mutableStateOf("") }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)) // transparent overlay
            .clickable { onDismiss() },                 // dismiss on outside tap
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth()

                .fillMaxHeight(0.7f),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFB0E0E6))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // 👇 Row for title + close button

                    Card(
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.45f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.kirana_shop),
                            contentDescription = "Kirana Shop",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd)
                        {
                            IconButton(onClick = { onDismiss() }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = shop.first.name,
                            maxLines = 1,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                fontStyle = FontStyle.Italic,
                                color = Color.Black
                            )
                        )


                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = report ,
                        onValueChange = {report = it},
                        Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(6.dp),
                        label = { Text(text = "Enter Report for this shop")},
                        shape = RoundedCornerShape(8.dp)
                    )



                }

                Button(
                    onClick = {
                        if (report.isNotBlank())
                        { val useremail = viewModel.useremail
                            val newReport = Report(useremail, report, Timestamp.now())
                            viewModel.addreport(id, newReport)

                        }},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("POST")
                }
            }
        }
    }
}
