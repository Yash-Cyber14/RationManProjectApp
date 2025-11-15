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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HOMESCREEN(userloc: userlocation?, navcontroller : NavHostController, darktheme : Boolean, Onthemechange : (Boolean)-> Unit) {

    val viewModel: locationviewmodel = hiltViewModel()
    val nearestshops by viewModel.nearestshops.collectAsState()
    val context = LocalContext.current

    val address by viewModel.address.collectAsState()

    var showDetails by remember { mutableStateOf(false) }
    var selectedshop by remember { mutableStateOf<Pair<apiresponse, Float>?>(null) }

    var showsidebar by rememberSaveable { mutableStateOf(false) }




    LaunchedEffect(userloc) {
        if (userloc != null) {
            viewModel.readableaddress(userloc, context)
            viewModel.lessdistanceshops(userloc)
        }
    }




    LaunchedEffect(Unit) {
        viewModel.uievents.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        }

    }


    var searchtext by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            sidesettings(
                darktheme = darktheme,
                Onthemechange = Onthemechange,
                navController = navcontroller
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                Column(Modifier.fillMaxSize()) {
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight().padding(10.dp),
                        shape = RoundedCornerShape(bottomStart = 9.dp, bottomEnd = 9.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x8027AE60))
                    ) {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(imageVector = Icons.Rounded.Menu, contentDescription = "")

                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    SearchBar(
                        query = searchtext,
                        onQueryChange = {
                            searchtext = it


                        },
                        onSearch = { active = false },

                        active = active,
                        onActiveChange = { active = it },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Search"
                            )

                        },
                        placeholder = { Text("Search...") },
                        colors = SearchBarDefaults.colors(
                            containerColor = Color(0x8027AE60), // Background of the whole SearchBar
//                    dividerColor = Color.Transparent // Optional: remove bottom divider Color(0x8027AE60)
                        )
                    ) {

                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x8027AE60),)
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 10.dp
                            )
                        ) {
                            Text(
                                text = "Quick Actions",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            LazyRow(modifier = Modifier.padding(vertical = 10.dp)) {
                                items(actionLists) { card ->
                                    ImageCardWithText(
                                        painter = painterResource(id = card.imageRes),
                                        contentDescription = card.contentDescription,
                                        title = card.title,
                                        navcontroller = navcontroller,
                                        route = card.route

                                    )
                                }
                            }

                        }

                    }
                    Spacer(modifier = Modifier.height(15.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x8027AE60)),
                        shape = RoundedCornerShape(6.dp)
                    ) {

                        Column(
                            Modifier
                                .fillMaxSize()
                                .background(Color(0x8027AE60))
                                .padding(horizontal = 8.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Nearest Shops",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            if (userloc == null) {
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(Color(0x8027AE60)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "SHOPS NOT AVAILABLE ! . LOCATION NOT FOUND")

                                }
                            } else {

                                LazyColumn(
                                    Modifier.padding(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(nearestshops) { shop ->
                                        shoplazycolumn(shop) {
                                            showDetails = true
                                            selectedshop = shop
                                        }

                                    }
                                }

                            }

                        }

                    }


                }
            }
            AnimatedVisibility(
                visible = showDetails,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()

            ) {
                selectedshop?.let { shopCard(shop = it) { showDetails = false } }

            }


        }
    }
}