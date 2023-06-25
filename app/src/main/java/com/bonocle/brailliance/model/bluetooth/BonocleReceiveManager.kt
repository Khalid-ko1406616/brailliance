package com.bonocle.brailliance.model.bluetooth

import kotlinx.coroutines.flow.MutableSharedFlow

interface BonocleReceiveManager {

    val data: MutableSharedFlow<Resource<BonocleResult>>

    fun sendPin(payload: ByteArray)

    fun reconnect()

    fun disconnect()

    fun startReceiving()

    fun closeConnection()

}