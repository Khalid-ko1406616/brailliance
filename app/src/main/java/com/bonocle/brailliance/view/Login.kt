package com.bonocle.brailliance.view

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bonocle.brailliance.R
import com.bonocle.brailliance.viewmodel.UserViewModel

@Composable
fun Login(
    onInstructor: () -> Unit, onStudent: () -> Unit) {

    val context = LocalContext.current

    val userViewModel =
        viewModel<UserViewModel>(viewModelStoreOwner = context as ComponentActivity)

    //VALUES STORING VARIABLES
    var email by remember { mutableStateOf("khalidomar92554@gmail.com") }
    var password by remember { mutableStateOf("test123") }

    fun quickCheck() {
        if (email == "" || password == "") {
            displayMessage(context, "Please enter all required fields")
        } else if (!email.contains("@")) {
            displayMessage(context, "Incorrect Email format")
        } else if (password.length < 6) {
            displayMessage(context, "Password must be 6 or more characters")
        } else {
            displayMessage(
                context,
                "Log in Failed, please try again later ${userViewModel.currentUser?.role} ${userViewModel.currentUser?.firstName}"
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Image(
            painter = painterResource(id = R.drawable.brailliance),
            contentDescription = "brailliance logo"
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = "E-mail")
            })

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(text = "Password")
            })

        Button(
            onClick = {
                userViewModel.signIn(email, password)
            },
            modifier = Modifier.fillMaxWidth()
                .padding(0.dp, 18.dp)
        ) {
            Text(
                text = "Login",
                fontWeight = FontWeight.ExtraBold
            )
        }

        if (userViewModel.currentUser != null) {
            if (userViewModel.currentUser!!.role == "Student") {
                onStudent()
            } else {
                onInstructor()
            }

        }

    }

}