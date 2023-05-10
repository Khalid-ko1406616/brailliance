package com.bonocle.brailliance.view

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bonocle.brailliance.viewmodel.SessionViewModel
import com.bonocle.brailliance.viewmodel.UserViewModel

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun Instructor(onAddSession: () -> Unit, onEditSession: () -> Unit){

    val sessionViewModel = viewModel<SessionViewModel>(viewModelStoreOwner = LocalContext.current as ComponentActivity)
    val sessionsList =sessionViewModel.sessionsList

    val userViewModel = viewModel<UserViewModel>(viewModelStoreOwner = LocalContext.current as ComponentActivity)
    val currentUser = userViewModel.currentUser

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = {
                Text(
                    text = " ${currentUser?.role} ${currentUser?.firstName}",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            })
        },
    ) {
        
    }

}