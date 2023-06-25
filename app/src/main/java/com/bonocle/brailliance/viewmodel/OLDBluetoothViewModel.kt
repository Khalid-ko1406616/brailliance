package com.bonocle.brailliance.viewmodel

import android.app.Application
import android.bluetooth.BluetoothAdapter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bonocle.brailliance.model.bluetooth.BonocleBLEReceiveManager
import com.bonocle.brailliance.model.bluetooth.ConnectionState
import com.bonocle.brailliance.model.bluetooth.Resource
import kotlinx.coroutines.launch

class OLDBluetoothViewModel (appContext: Application) :AndroidViewModel(appContext){

    private val bluetoothAdapter: BluetoothAdapter
        get() {
            TODO()
        }

    private val bonocleReceiveManager = BonocleBLEReceiveManager(bluetoothAdapter,appContext)

    var initialzingMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var button by mutableStateOf(0)
        private set

    var current = 0

    var isStart = true

    var connectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)

    private fun subscribeToChanges(){
        viewModelScope.launch {
            bonocleReceiveManager.data.collect{ result ->
                when(result){
                    is Resource.Success -> {
                        connectionState = result.data.connectionState
                        button = result.data.Button
//                        isPressed = result.data.isPressed
                    }
                    is Resource.Loading -> {
                        initialzingMessage = result.message
                        connectionState = ConnectionState.CurrentlyInitializing
                    }
                    is Resource.Error -> {
                        errorMessage = result.errorMessage
                        connectionState = ConnectionState.Uninitialized
                    }
                }
            }
        }
    }

    fun sendPin(letter : String){
        if(letter != "FF"){
            val pin = letter.decodeHex()
            bonocleReceiveManager.sendPin(pin)
        }else{
            bonocleReceiveManager.sendPin("FF".decodeHex())
        }

    }

    fun String.decodeHex(): ByteArray {
        check(length % 2 == 0) {error("Must have an even length")}

        return chunked(2)
            .map {it.toInt(16).toByte()}
            .toByteArray()
    }

    fun disconnect(){
        bonocleReceiveManager.disconnect()
    }

    fun reconnect(){
        bonocleReceiveManager.reconnect()
    }

    fun initializeConnection(){
        errorMessage = null
        subscribeToChanges()
        bonocleReceiveManager.startReceiving()
    }

    override fun onCleared() {
        super.onCleared()
        bonocleReceiveManager.closeConnection()
    }

}