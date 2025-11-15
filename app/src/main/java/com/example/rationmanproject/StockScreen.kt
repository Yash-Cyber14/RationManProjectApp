package com.example.rationmanproject

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun stockscreen(userloc : userlocation?, navController: NavController){

    var searchstock by remember {mutableStateOf("")}
    val viewModel : locationviewmodel = hiltViewModel()
    var active by remember {mutableStateOf(false)}

    val shops by viewModel.allshops.collectAsState()

    var showDetails by remember { mutableStateOf(false) }
    var selectedshop by remember { mutableStateOf<Pair<apiresponse,Float>?>(null) }

    var filterItems by remember {
        mutableStateOf(
            listOf(
                filters("ALL", true),
                filters("RICE", false),
                filters("WHEAT", false),
                filters("DAL", false),
                filters("SUGAR", false)
            )
        )
    }

    LaunchedEffect(userloc , filterItems , searchstock){
        val activefilter = filterItems.firstOrNull(){it.enabled}?.name ?:"ALL"
        if (userloc != null) {
            viewModel.filtershops(userloc , searchstock , activefilter)
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
        Image(painter = painterResource(id = R.drawable.screenhome), contentDescription ="" ,Modifier.fillMaxSize(), contentScale = ContentScale.FillHeight )
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xCC000000))
        ){
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)) {

                IconButton(onClick = { navController.navigate(Screen.homescreen.route) }) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)


                }
                Spacer(modifier = Modifier.height(10.dp))

                var searchText by remember { mutableStateOf("") }
                val viewModel: locationviewmodel = hiltViewModel()

                TextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        if (userloc != null) {
                            val activeFilter = filterItems.firstOrNull { f -> f.enabled }?.name ?: "ALL"
                            viewModel.filtershops(userloc, searchText, activeFilter)
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

                if (userloc == null) {
                    Text(text = "Location not Found")
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(filterItems.size) { index ->
                            val item = filterItems[index]
                            FilterBox(item, onClick = {
                                filterItems = filterItems.mapIndexed { i, f ->
                                    f.copy(enabled = i == index)
                                }
                            })

                        }

                    }
                }


                Spacer(modifier = Modifier.height(15.dp))


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .background(color = Color(0xFF1b5e20))
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        items(shops) { shop ->
                            shoplazycolumn(shop = shop) {
                                showDetails = true
                                selectedshop = shop

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

@Composable
fun FilterBox(filter : filters, onClick : ()->Unit ){
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (filter.enabled) Color(0xFF004d40) else Color(0xFF66bb6a))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = "${filter.name}", color = Color.White ,style = MaterialTheme.typography.bodyMedium)


    }

}

data class filters(
    val name: String,
    var enabled: Boolean
)