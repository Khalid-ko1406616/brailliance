package com.bonocle.brailliance.view

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Login
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bonocle.brailliance.R
import com.bonocle.brailliance.viewmodel.UserViewModel

@Composable
fun Login(
    onInstructor: () -> Unit,
    onStudent: () -> Unit,
    onRegister: ()-> Unit) {

    val context = LocalContext.current

    val userViewModel =
        viewModel<UserViewModel>(viewModelStoreOwner = context as ComponentActivity)

    //VALUES STORING VARIABLES
    var email by remember { mutableStateOf("khalidomar92554@gmail.com") }
    var password by remember { mutableStateOf("test123") }

    var checkRemember by remember { mutableStateOf(false)}


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Spacer(modifier = Modifier.padding(8.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.brailliance),
                contentDescription = "brailliance logo"
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it
                                userViewModel.errorMessage = ""},
                label = { Text( text = "Email" ) },
                placeholder = { Text(text = "Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
                isError = userViewModel.errorMessage.isNotEmpty()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it
                    userViewModel.errorMessage = ""},
                placeholder = { Text(text = "Password") },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
                isError = userViewModel.errorMessage.isNotEmpty()
            )
            Button(
                onClick = {
                    userViewModel.signIn(email, password)
                },
                modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
            ) {
                Text(text = "Login")
            }

            if (userViewModel.currentUser != null && !checkRemember)
                if(userViewModel.currentUser!!.role == "Student"){
                    checkRemember = !checkRemember
                    onStudent()
                }else{
                    checkRemember = !checkRemember
                    onInstructor()
                }

            if (userViewModel.errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = userViewModel.errorMessage, style = TextStyle(color = Color.Red))
                displayMessage(message = userViewModel.errorMessage)
            }



            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Register",
                style = TextStyle(color = Color.Blue, textDecoration = TextDecoration.Underline),
                modifier = Modifier.clickable {
                    onRegister()
                }
            )
        }
    }
    

}