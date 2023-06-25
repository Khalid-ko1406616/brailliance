package com.bonocle.brailliance.view.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*

import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String , val title: String, val icon: ImageVector){

    object Landing : Screen(route = "landing_screen", title = "Landing", icon = Icons.Outlined.DoorFront)

    object Login : Screen(route = "login_screen", title = "Login", icon = Icons.Outlined.Login)

    object Register : Screen(route = "register_screen", title = "Register", icon = Icons.Outlined.PersonAdd)

    object InstructorHomepage : Screen(route = "instructor_screen", title = "Instructor", icon = Icons.Outlined.Home)

    object StudentHomepage : Screen(route = "student_screen", title = "Student", icon = Icons.Outlined.HomeWork)

    object SessionForm : Screen(route = "session_form" , title = "Session Form", icon = Icons.Outlined.Class)

    object Session : Screen(route = "session_screen", title = "Session", icon = Icons.Outlined.VideoCall)

    object Settings : Screen(route = "settings_screen", title = "Settings", icon = Icons.Outlined.Settings)

    object Bluetooth : Screen(route = "bluetooth_screen", title = "Bluetooth", icon = Icons.Outlined.Bluetooth)

    object Profile : Screen(route = "profile_screen", title = "Profile", icon = Icons.Outlined.VerifiedUser)

    object Logout : Screen(route = "", title = "Logout", icon = Icons.Outlined.Logout)

    object Divider : Screen(route = "", title = "Divider", icon = Icons.Outlined.Minimize)
}