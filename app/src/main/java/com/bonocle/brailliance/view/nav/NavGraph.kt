package com.bonocle.brailliance.view.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bonocle.brailliance.view.*


@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String
){

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(Screen.Landing.route){
            LandingPage(
                onLogin = {navController.navigate(Screen.Login.route)},
                onRegister = {navController.navigate(Screen.Register.route)}
            )
        }
        composable(Screen.Login.route){
            Login(
                onInstructor = {navController.navigate(Screen.InstructorHomepage.route)},
                onStudent = {navController.navigate(Screen.StudentHomepage.route)}
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
                onAddSession = {},
                onEditSession = {}
            )
        }
        composable(Screen.StudentHomepage.route){
            Student()
        }
    }

}