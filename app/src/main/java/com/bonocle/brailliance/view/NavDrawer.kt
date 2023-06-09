package com.bonocle.brailliance.view

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bonocle.brailliance.R
import com.bonocle.brailliance.view.nav.Screen
import com.bonocle.brailliance.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun NavDrawer(navController: NavController,
              coroutineScope: CoroutineScope,
              scaffoldState: ScaffoldState
){
    val userViewModel =  viewModel<UserViewModel>(viewModelStoreOwner = LocalContext.current as ComponentActivity)

    val navDrawerItems = mutableListOf<Screen>(
        Screen.Divider
    )

    val currentRoute = getCurrentRoute(navController = navController)

    Column {
        Image(
            painter = painterResource(id = R.drawable.brailliance),
            contentDescription = "brailliance logo"
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(5.dp))

        userViewModel.currentUser?.let {
            navDrawerItems.add(0,  Screen.Bluetooth)
            navDrawerItems.add(1,  Screen.Divider)
            navDrawerItems.add(2,Screen.Settings)
            navDrawerItems.add(3,Screen.Divider)
            navDrawerItems.add(4,Screen.Logout)
            Text(
                text = "Welcome ${it.role} ${it.firstName} ${it.lastName}",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } ?: navDrawerItems.add(0,  Screen.Login)

        navDrawerItems.forEach { item ->
            DrawerItem(item = item, selected = currentRoute == item.route,
                onItemClick = {
                    var routeTo = it
                    if (it == Screen.Logout) {
                        userViewModel.signOut()
//                        userViewModel.errorMessage = ""
                        routeTo = Screen.Landing
                    }
                    navController.navigate(routeTo.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route)
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                    }
                    // Close drawer
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
                )
        }

    }

}

@Composable
fun DrawerItem(item: Screen, selected: Boolean, onItemClick: (Screen) -> Unit) {
    val background = if (selected)
        MaterialTheme.colors.primaryVariant
    else
        MaterialTheme.colors.primarySurface

    if (item.title == "Divider") {
        Divider(Modifier.height(10.dp))
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onItemClick(item) })
                .height(45.dp)
                .background(background)
                .padding(start = 10.dp)
        ) {
            Image(
                imageVector = item.icon,
                contentDescription = item.title,
                colorFilter = ColorFilter.tint(Color.White),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(35.dp)
                    .width(35.dp)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = item.title,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}
