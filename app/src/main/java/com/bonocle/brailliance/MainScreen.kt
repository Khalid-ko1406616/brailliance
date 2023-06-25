package com.bonocle.brailliance

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bonocle.brailliance.view.NavDrawer
import com.bonocle.brailliance.view.displayMessage
import com.bonocle.brailliance.view.getCurrentRoute
import com.bonocle.brailliance.view.nav.Screen
import com.bonocle.brailliance.view.nav.AppNavigator
import com.bonocle.brailliance.viewmodel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(){

    val context = LocalContext.current
    val authViewModel =
        viewModel<UserViewModel>(viewModelStoreOwner = LocalContext.current as ComponentActivity)
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val currentRoute = getCurrentRoute(navController)

    var startDestination by remember { mutableStateOf(Screen.Landing.route)}

    LaunchedEffect(true) {

        authViewModel.setCurrentUser()

        Firebase.auth.addAuthStateListener {
            println(">> Debug: Firebase.auth.addAuthStateListener: ${it.currentUser?.email}")
           if (it.currentUser != null) {
                "Welcome ${it.currentUser!!.displayName}"
                displayMessage(context,message = "Welcome ${it.currentUser!!.displayName}")
            } else {
                 //"Signed out successfully."
            }

        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (currentRoute != Screen.Login.route) {
                TopBar(coroutineScope, scaffoldState)
            }
        },
        bottomBar = {
//            if (currentRoute == Screen.Landing.route)
//                BottomNavBar(navController)
        },
        drawerContent = {NavDrawer(navController, coroutineScope, scaffoldState)}
    ) {
        paddingValues ->
        AppNavigator(navController, paddingValues, startDestination )
    }

}


/**
 * It receives navController to navigate between screens
 */
@Composable
fun BottomNavBar(navController: NavHostController) {
    BottomAppBar {
        //observe current route to change the icon color,label color when navigated
        val authViewModel =
            viewModel<UserViewModel>(viewModelStoreOwner = LocalContext.current as ComponentActivity)
        val currentRoute = getCurrentRoute(navController)

//        val navItems = listOf(Screen.ShoppingList, Screen.CloudStorage) //, Screen.Login)

//        navItems.forEach { navItem ->
//            BottomNavigationItem(
//                //if currentRoute is equal to the nav item route then set selected to true
//                selected = currentRoute == navItem.route,
//                onClick = {
//                    navController.navigate(navItem.route) {
//                        /* Navigate to the destination only if we’re not already on it,
//                        avoiding multiple copies of the destination screen on the back stack */
//                        launchSingleTop = true
//                    }
//                },
//                icon = {
//                    Icon(imageVector = navItem.icon, contentDescription = navItem.title)
//                },
//                label = {
//                    Text(text = navItem.title)
//                },
//                alwaysShowLabel = false
//            )
//        }
    }
}

//A function which will receive a callback to trigger to opening the drawer
@Composable
fun     TopBar(coroutineScope: CoroutineScope, scaffoldState: ScaffoldState) {
    TopAppBar(
        title = {
            Text(text = "Brailliance")
        },
        //Provide the navigation Icon ( Icon on the left to toggle drawer)
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier.clickable(onClick = {
                    //When the icon is clicked open the drawer in coroutine scope
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                })
            )
        }
    )
}