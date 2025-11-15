package com.example.rationmanproject

import android.widget.Toast
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun appentrypoint(navController: NavController, sharedViewModel: SharedViewModel , viewModel : locationviewmodel) {


    Box(modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center){
        CircularProgressIndicator()

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp) , contentAlignment = Alignment.BottomEnd){
            Text(text = "LOGGING IN ...." , style = TextStyle(fontWeight = FontWeight.SemiBold , fontStyle = FontStyle.Italic , fontSize = 15.sp))

        }

    }

//    FirebaseAuth.getInstance().signOut()  //for testing purpose

    val context = LocalContext.current
    val afterloggingscreen = viewModel.afterloggingscreen.collectAsState()
    var userLoc by remember { mutableStateOf<userlocation?>(null) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                fetchLocation(context) { loc ->
                    userLoc = loc
                    sharedViewModel.setUserLocation(loc)
                    navigateAfterLocation(navController)
                }
            } else {
                Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    LaunchedEffect(Unit) {
        when {
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED -> {
                fetchLocation(context) { loc ->
                    userLoc = loc
                    sharedViewModel.setUserLocation(loc)
                    navigateAfterLocation(navController)
                }
            }
            else -> {
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
    LaunchedEffect(true){
        viewModel.loggingstatus()


    }

    LaunchedEffect(afterloggingscreen.value) {
        delay(1000)
        afterloggingscreen.value?.let { navController.navigate(it.route) }
    }

}

fun fetchLocation(context: android.content.Context, onLocationFetched: (userlocation) -> Unit) {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        val client = LocationServices.getFusedLocationProviderClient(context)
        client.lastLocation.addOnSuccessListener { loc ->
            loc?.let {
                onLocationFetched(userlocation(it.latitude, it.longitude))
            }
        }
    } else {
        Log.e("FetchLocation", "Permission not granted!")
    }
}


fun navigateAfterLocation(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    if (auth.currentUser != null) {
        navController.navigate(Screen.homescreen.route) {
            popUpTo(Screen.Appentrypoint.route) { inclusive = true }
        }
    } else {
        navController.navigate(Screen.Signup.route) {
            popUpTo(Screen.Appentrypoint.route) { inclusive = true }
        }
    }
}


data class userlocation(
    val latitude : Double,
    val longitude : Double
)