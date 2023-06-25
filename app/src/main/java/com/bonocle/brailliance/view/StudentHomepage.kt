package com.bonocle.brailliance.view

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bonocle.brailliance.viewmodel.SessionViewModel
import com.bonocle.brailliance.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Student( onSession: () -> Unit ){
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ){
        Card(Modifier.fillMaxWidth(0.9f).background(Color.LightGray)) {
            Text("Subjects",Modifier.align(Alignment.CenterHorizontally))
            LazyColumn(content = {

            })
        }

        Card(Modifier.fillMaxWidth(0.9f).background(Color.LightGray).align(Alignment.CenterHorizontally)) {
            Text("Your Sessions",Modifier.align(Alignment.CenterHorizontally))
            LazyColumn(content = {

            })
        }

    }

}
