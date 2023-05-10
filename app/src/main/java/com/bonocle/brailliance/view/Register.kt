package com.bonocle.brailliance.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.app.ComponentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bonocle.brailliance.R
import com.bonocle.brailliance.model.User
import com.bonocle.brailliance.viewmodel.UserViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Register(
    onSubmit: () -> Unit,
){

    val context = LocalContext.current

    val userViewModel =
        viewModel<UserViewModel>(viewModelStoreOwner = context as androidx.activity.ComponentActivity)

//VALUES STORING VARIABLES
    var email by remember { mutableStateOf("khalidomar92554@gmail.com") }
    var firstName by remember { mutableStateOf("Khalid") }
    var lastName by remember { mutableStateOf("Omar") }
    var password by remember { mutableStateOf("test123") }
    var role by remember { mutableStateOf("Instructor")}

    fun onRegister() {
        val user = User(
            uid = "",
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            role = role
        )

//        userViewModel.signIn(user.email,user.password)
//        userViewModel.setCurrentUser()
//
//        if(userViewModel.currentUser != null){
//            displayMessage(context,"Email already in use.")
//            return
//        }
//
        if (firstName == "" || lastName == "" || email == "" || password == "" || role == "") { // check if al fields entered
            displayMessage(context, "Please enter all required fields")
            return
        }
//        else if (!email.contains("@")){
//            displayMessage(context,"Incorrect Email format")
//            return
//        }else if (password.length < 6){
//            displayMessage(context,"Password must be 6 or more characters")
//            return
//        }

        try {
            userViewModel.register(user)
            onSubmit()
        }catch (e: Exception) {
            displayMessage(context, message = e.message ?: "Register Failed.")
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ){
        Image(painter = painterResource(id = R.drawable.brailliance), contentDescription = "brailliance logo")

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = "E-mail")
            })

        Spacer(modifier = Modifier.padding(8.dp))
        OutlinedTextField(
            value = firstName,
            onValueChange = {
                firstName = it
            },
            label = {
                Text(text = "First Name")
            })

        OutlinedTextField(
            value = lastName,
            onValueChange = {
                lastName = it
            },
            label = {
                Text(text = "Last Name")
            })

        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(text = "Password")
            })

        Spacer(modifier = Modifier.padding(8.dp))

        val listItems = arrayOf("Instructor","Student")
        var expanded by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableStateOf(listItems[0])}

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {role = selectedItem},
                readOnly = true,
                label = { Text(text = "Role")},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                listItems.forEach { selectedOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedItem = selectedOption
                            expanded = false
                        }) {
                        Text(text = selectedOption)
                    }
                }

            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            onClick = {
                role = selectedItem
                onRegister()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 18.dp)
        ) {
            Text(
                text = "Register",
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}