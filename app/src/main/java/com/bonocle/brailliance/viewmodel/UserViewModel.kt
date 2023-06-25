package com.bonocle.brailliance.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonocle.brailliance.model.User
import com.bonocle.brailliance.model.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val userRepository = UserRepository()

    var currentUser by mutableStateOf<User?>(null)
    var errorMessage by mutableStateOf("")

    var rememberCheck by mutableStateOf(false)

    private val exceptionHandler = CoroutineExceptionHandler { context, exception ->
        this.errorMessage = exception.message ?: "Request failed"
        println(">> Debug: Exception thrown: $exception.")
    }

    fun register(user:User) = viewModelScope.launch(exceptionHandler){
        currentUser = userRepository.register(user)
    }

    fun signIn(email: String, password: String) = viewModelScope.launch(exceptionHandler) {
        errorMessage = ""
        currentUser = null
        currentUser = userRepository.signIn(email,password)
    }



    fun setCurrentUser() = viewModelScope.launch(exceptionHandler){
        val uid = Firebase.auth.currentUser?.uid
        if(uid == null)
            currentUser = null
        else {
            var user = userRepository.getUser(uid)
            if(user == null) {
                val displayName = Firebase.auth.currentUser?.displayName?: ""
                val email = Firebase.auth.currentUser?.email?: ""
                user = User(uid, displayName, email)
            }
            currentUser = user
        }
    }

    fun signOut() {
        Firebase.auth.signOut()
        this.currentUser = null
    }

}