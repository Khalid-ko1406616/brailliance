package com.bonocle.brailliance.view.nav

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bonocle.brailliance.view.*
import com.bonocle.brailliance.viewmodel.UserViewModel


@Composable
fun AppNavigator(
    navController: NavHostController,
    padding: PaddingValues,
    startDestination: String
){
    val userViewModel =  viewModel<UserViewModel>(viewModelStoreOwner = LocalContext.current as ComponentActivity)

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(paddingValues = padding)
    ){
        composable(Screen.Landing.route){
            if(userViewModel.currentUser != null && userViewModel.rememberCheck)
                if(userViewModel.currentUser!!.role == "Student"){
                    userViewModel.rememberCheck = !userViewModel.rememberCheck
                    navController.navigate(Screen.StudentHomepage.route)
                }else{
                    userViewModel.rememberCheck = !userViewModel.rememberCheck
                    navController.navigate(Screen.InstructorHomepage.route)
                }

            LandingPage(
                onLogin = {navController.navigate(Screen.Login.route)},
                onRegister = {navController.navigate(Screen.Register.route)}
            )
        }
        composable(Screen.Login.route){
            Login(
                onInstructor = {navController.navigate(Screen.InstructorHomepage.route)},
                onStudent = {navController.navigate(Screen.StudentHomepage.route)},
                onRegister = {navController.navigate(Screen.Register.route)}
            )
        }
        composable(Screen.Register.route){
            Register(
                onSubmit = {navController.navigate(Screen.Login.route){
                    popUpTo(Screen.Register.route){
                        inclusive = true
                    }
                } }
            )
        }
        composable(Screen.InstructorHomepage.route){
            Instructor(
                onAddSession = {Screen.SessionForm.route},
                onEditSession = {},
                onSession = {}
            )
        }
        composable(Screen.StudentHomepage.route){
            Student(
                onSession = {}
            )
        }
        composable(Screen.Bluetooth.route){

        }
        composable(Screen.Profile.route){

        }
        composable(Screen.Session.route){

        }
        composable(Screen.SessionForm.route){
            SessionForm()
        }
        composable(Screen.Settings.route){

        }
    }

}