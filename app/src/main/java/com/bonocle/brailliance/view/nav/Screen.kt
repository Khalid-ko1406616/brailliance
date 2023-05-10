package com.bonocle.brailliance.view.nav

sealed class Screen(val route: String){

    object Landing : Screen(route = "landing_screen")

    object Login : Screen(route = "login_screen")

    object Register : Screen(route = "register_screen")

    object InstructorHomepage : Screen(route = "instructor_screen")

    object StudentHomepage : Screen(route = "student_screen")
}