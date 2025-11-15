package com.example.rationmanproject

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(userloc: userlocation?, navcontroller : NavHostController , darktheme : Boolean ,Onthemechange : (Boolean)-> Unit) {

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF1b5e20), Color(0xFF81c784)),
                        endY = 550.0f
                    )
                )
        ) {

            Column(Modifier.fillMaxSize()) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(9.dp) , colors = CardDefaults.cardColors(containerColor = Color(0xFF81c784)),
                    shape = RoundedCornerShape(bottomStart = 14.dp, bottomEnd = 14.dp)
                ) {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Rounded.Menu, contentDescription = "menu")
                    }
                }

                Box(modifier = Modifier.fillMaxSize())
                {
                    Image(
                        painter = painterResource(id = R.drawable.rationbackground),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )

                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Box {
//                        Row {
//                            IconButton(onClick = {
//                                scope.launch {
//                                    drawerState.open()
//                                }
//                            }) {
//                                Icon(imageVector = Icons.Rounded.Menu, contentDescription = "menu")
//                            }
//
//                        }
//                    }
//
//
//
//                }

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
                                    containerColor = Color(0xFF1b5e20), // Background of the whole SearchBar
//                    dividerColor = Color.Transparent // Optional: remove bottom divider
                                )
                            ) {

                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1b5e20),)
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
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1b5e20)),
                                shape = RoundedCornerShape(6.dp)
                            ) {

                                Column(
                                    Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFF1b5e20))
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
                                                .background(Color(0xFF1b5e20)),
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
}




@Composable
fun shoplazycolumn(shop: Pair<apiresponse, Float> ,onClick : () -> Unit){



    Card(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .height(70.dp), elevation = CardDefaults.cardElevation() , colors = CardDefaults.cardColors(containerColor = Color(0x8032CD32))) {
        Row(Modifier.fillMaxSize() , horizontalArrangement = Arrangement.SpaceBetween , verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.fillMaxSize()){
                Card(
                    Modifier
                        .fillMaxHeight()
                        .width(100.dp)) {
                    Image(painter = painterResource(id = R.drawable.kirana_shop), contentDescription = "Kirana Shop", contentScale = ContentScale.Crop,modifier = Modifier.fillMaxSize())

                }
                Spacer(modifier = Modifier.width(20.dp))
                Column(Modifier.padding(6.dp)){
                    Text(text = "${shop.first.name}", maxLines = 1, style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic
                    ))

                }


            }



        }

    }




}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun shopCard(
    shop: Pair<apiresponse, Float>,
    onDismiss: () -> Unit = {}
) {
    var show by remember { mutableStateOf(false) }
    val sheetstate = rememberModalBottomSheetState( skipPartiallyExpanded = false)
    val viewModel : locationviewmodel = hiltViewModel()
    val id = shop.first.id

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
                .padding(horizontal = 10.dp)
                .fillMaxHeight(0.7f),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF4FC3F7))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
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

                        Box(modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.TopEnd)
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


                    Text(
                        text = "Distance : ${shop.second} m",
                        style = TextStyle(
                            fontWeight = FontWeight.Thin,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Timings : ${shop.first.timings}",
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = shop.first.phone,
                        style = TextStyle(
                            fontWeight = FontWeight.Thin,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    )
                }

                Button(
                    onClick = { show = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Show Reports")
                }
            }
        }
    }

    LaunchedEffect(id){
        viewModel.getReports(id)
    }



    if(show){
        ModalBottomSheet(
            onDismissRequest = { show = false },
            sheetState = sheetstate
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 250.dp)
                    .background(Color(0xFF1b5e20))
            ) {
                val reportdata by viewModel.reports.collectAsState()

                if (reportdata.isEmpty()) {
                    // Placeholder when no reports
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No reports available")
                    }
                } else {
                    LazyColumn(modifier = Modifier.padding(10.dp) , verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(reportdata ) { report ->
                            reportbar(report)
                        }
                    }
                }
            }
        }


    }

}

@Composable
fun reportbar(report: Report){

    val date = remember(report.timestamp){
        val sdf = SimpleDateFormat("dd mm yyyy , hh:mm a" , Locale.getDefault())
        sdf.format(report.timestamp?.toDate() ?: "")
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(7.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF81c784) , contentColor = Color.Black)
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(3.dp) , horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = report.username, style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 13.sp))
                Text(text = date, style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 12.sp))


            }
            Spacer(Modifier.height(6.dp))
            Text(text = report.message, Modifier.padding(4.dp) , fontStyle = FontStyle.Italic)
        }


    }


}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCardWithText(
    painter: Painter,
    contentDescription: String,
    title: String,
    navcontroller: NavHostController,
    route: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 10.dp)
            .width(100.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        onClick = {navcontroller.navigate(route)}
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            endY = 200.0f,
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                        )
                    ),


                ){

                Text(
                    text = title,
                    style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.SemiBold),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                )
            }

            // Text on top
        }
    }
}


@Composable
fun sidesettings(darktheme: Boolean , Onthemechange : (Boolean) -> Unit , navController: NavController) {

    Card(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.7f),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4FC3F7),
            contentColor = Color.Black
        ),

        ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = Color.Black,
                    modifier = Modifier.size(60.dp)
                )

            }
            Spacer(modifier = Modifier.height(40.dp))

            Column(Modifier.fillMaxSize()) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable { navController.navigate(Screen.ProfileScreen.route) }, elevation = CardDefaults.cardElevation(5.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        Modifier
                            .height(45.dp)
                            .fillMaxWidth(),

                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(imageVector = Icons.Rounded.AccountCircle, contentDescription = "Profile")
                        Text(text = "Profile")


                    }

                }
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                    , elevation = CardDefaults.cardElevation(5.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        Modifier
                            .height(45.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Dark Mode")
                        Switch(checked = darktheme, onCheckedChange = Onthemechange)

                    }

                }


            }

        }
    }
}


















data class CardDetails(
    @DrawableRes val imageRes: Int,
    val title: String,
    val contentDescription: String,
    val route :String
)

val actionLists = listOf(
    CardDetails(R.drawable.check_stocks, "STOCK", "Stocks" , Screen.StocksScreen.route),
    CardDetails(R.drawable.subsidy_status, "SUBSIDY", "Check Subsidy",Screen.Subsidyscreen.route),
    CardDetails(R.drawable.stock_report, "REPORT", "ReportShop" ,Screen.ReportScreen.route)
)