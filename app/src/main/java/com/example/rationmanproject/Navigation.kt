package com.example.rationmanproject

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable


import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.firebase.auth.FirebaseAuth

sealed class Screen(val route : String){

    object Signin : Screen("Signin")

    object Signup : Screen("Signup")

    object loggingscreen : Screen("Loggingscreen")

    object homescreen : Screen("homescreen")

    object StocksScreen : Screen("StocksScreen")

    object ReportScreen : Screen("ReportScreen")

    object Subsidyscreen : Screen("subsidyscreen")

    object Appentrypoint : Screen("entry")

    object ProfileScreen : Screen("profilescreen")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun signnavigation(sharedViewModel: SharedViewModel = hiltViewModel() , darktheme : Boolean , Onthemechange : (Boolean) -> Unit , viewModel : locationviewmodel){

    val auth = FirebaseAuth.getInstance()

    val navcontroller = rememberNavController()
    val viewModel1 : AuthViewModel = viewModel()
    AnimatedNavHost(navController = navcontroller , startDestination = Screen.Appentrypoint.route){



        composable(Screen.loggingscreen.route){
            Loggingscreen(navController = navcontroller)
        }

        composable(Screen.Appentrypoint.route) {
            appentrypoint(navcontroller,sharedViewModel = sharedViewModel , viewModel)
        }

        composable(Screen.Signup.route,
            enterTransition = {slideInVertically() + fadeIn()},
            exitTransition = { slideOutVertically() + fadeOut() }

        ){
            SignUp(navController = navcontroller)

        }
        composable(Screen.Signin.route,
            enterTransition = {slideInVertically() + fadeIn()},
            exitTransition = { slideOutVertically() + fadeOut() }
        ){
            SignIn(navcontroller)
        }
        composable(Screen.homescreen.route,
            enterTransition = {slideInHorizontally() + fadeIn()},
            exitTransition = { slideOutHorizontally() + fadeOut() }
        ){
            val userloc by sharedViewModel.userLocation.collectAsState()
            HOMESCREEN(userloc = userloc , navcontroller , darktheme , Onthemechange )


        }
        composable(Screen.StocksScreen.route,
            enterTransition = {slideInHorizontally() + fadeIn()},
            exitTransition = { slideOutHorizontally() + fadeOut() }
        ){
            val userloc by sharedViewModel.userLocation.collectAsState()
            stockscreen(userloc = userloc ,navcontroller)
        }
        composable(Screen.ReportScreen.route,
            enterTransition = {slideInHorizontally() + fadeIn()},
            exitTransition = { slideOutHorizontally() + fadeOut() }
        ){
            val userloc by sharedViewModel.userLocation.collectAsState()
            ReportScreen(userloc = userloc, navController = navcontroller)
        }

        composable(Screen.Subsidyscreen.route,
            enterTransition = {slideInHorizontally() + fadeIn()},
            exitTransition = { slideOutHorizontally() + fadeOut() }
        ){
            SubsidyCheckScreen(navcontroller)
        }

        composable(Screen.ProfileScreen.route,
            enterTransition = {slideInHorizontally() + fadeIn()},
            exitTransition = { slideOutHorizontally() + fadeOut() }
        ){
            ProfileScreen(
                navController = navcontroller,
                viewModel = viewModel1
            )

        }


    }





}