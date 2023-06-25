package com.bonocle.brailliance.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bonocle.brailliance.R.drawable.brailliance

@Composable
fun LandingPage(
    onLogin: () -> Unit,
    onRegister: () -> Unit
){

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Image(painter = painterResource(id = brailliance), contentDescription = "brailliance logo")

        Row() {
            Column() {
                Button(
                    onClick = { onLogin() },
                    modifier = Modifier.fillMaxWidth()
                        .padding(0.dp,18.dp)
                ) {
                    Text(
                        text = "Login",
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Button(onClick = { onRegister() },
                    modifier = Modifier.fillMaxWidth()
                        .padding(0.dp,18.dp)) {
                    Text(
                        text = "Register",
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun preview(){
//    LandingPage(onLogin = {}) {
//
//    }
//}