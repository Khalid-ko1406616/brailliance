package com.bonocle.brailliance

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bonocle.brailliance.view.displayMessage
import com.bonocle.brailliance.view.getCurrentRoute
import com.bonocle.brailliance.view.nav.Screen
import com.bonocle.brailliance.view.nav.SetupNavGraph
import com.bonocle.brailliance.viewmodel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(){

    val context = LocalContext.current
    val authViewModel =
        viewModel<UserViewModel>(viewModelStoreOwner = LocalContext.current as ComponentActivity)
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val currentRoute = getCurrentRoute(navController)
    var startDestination by remember { mutableStateOf(Screen.Landing.route)}

    LaunchedEffect(true) {

        authViewModel.setCurrentUser()
        Firebase.auth.addAuthStateListener {
            println(">> Debug: Firebase.auth.addAuthStateListener: ${it.currentUser?.email}")
            val message = if (it.currentUser != null) {
                "Welcome ${it.currentUser!!.displayName}"
            } else {
                "Welcome." //"Signed out successfully."
            }
            displayMessage(context,message = message)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {

        },
        bottomBar = {

        },
        drawerContent = {

        }
    ) {
    SetupNavGraph(navController = navController, startDestination = startDestination)
    }

}