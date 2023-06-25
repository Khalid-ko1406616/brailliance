package com.bonocle.brailliance.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.core.app.ComponentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bonocle.brailliance.model.Session
import com.bonocle.brailliance.viewmodel.SessionViewModel
import com.bonocle.brailliance.viewmodel.UserViewModel

enum class FormMode { ADD, EDIT}

@Composable
fun SessionForm(){

    var formMode = FormMode.ADD

    var screenTitle = "Add Session"

    var confirmButtonLabel = "Add"

    val sessionViewModel = viewModel<SessionViewModel>(viewModelStoreOwner = LocalContext.current as androidx.activity.ComponentActivity)
    val userViewModel = viewModel<UserViewModel>(viewModelStoreOwner = LocalContext.current as androidx.activity.ComponentActivity)


    val context = LocalContext.current

    var subject by remember { mutableStateOf(sessionViewModel.selectedSession?.subject ?: "")}
    var meetingTime by remember { mutableStateOf(sessionViewModel.selectedSession?.meetingTime ?: "")}
    var instructorID by remember { mutableStateOf(sessionViewModel.selectedSession?.instructorID ?: "")}
    var studentID by remember { mutableStateOf(sessionViewModel.selectedSession?.studentID ?: "")}

    val calendar = Calendar.getInstance()

    var selectedDateText by remember { mutableStateOf("") }

    var selectedTimeText by remember { mutableStateOf("")}

// Fetching current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
        }, year, month, dayOfMonth
    )

    datePicker.datePicker.minDate = calendar.timeInMillis


    // Fetching current hour, and minute
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val timePicker = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            selectedTimeText = "$selectedHour:$selectedMinute"
        }, hour, minute, false
    )

    if (sessionViewModel.selectedSession != null) {
        formMode = FormMode.EDIT
        screenTitle = "Edit Session"
        confirmButtonLabel = "Update"
    }

    OutlinedTextField(
        value = subject,
        onValueChange = {
            subject = it
        },
        label = {
            Text(text = "Subject")
        },
        placeholder = { Text(text = "Subject") },
        singleLine = true)
    
    Row() {
        Column() {
            Text("One-Time")
            RadioButton(selected = true,onClick = { /*TODO*/ })
            Text("Weekly")
            RadioButton(selected = false, onClick = { /*TODO*/ })
        }
        Button(onClick = { datePicker.show() }) {
            Text("Day")
        }
        Button(onClick = { timePicker.show() }) {
            Text("Time")
        }

    }

    Button(onClick = {

        if(formMode == FormMode.ADD){

            val session = Session(
                subject = subject,
                meetingTime = "",/*TODO*/
                instructorID = userViewModel.currentUser?.uid ?: ""
            )

            sessionViewModel.addSession(session)

        }else{
            sessionViewModel.selectedSession?.let {
                it.subject = subject
                it.meetingTime = "" /*TODO*/
                it.instructorID = userViewModel.currentUser?.uid ?: ""
            }
        }

    }) {

    }


}