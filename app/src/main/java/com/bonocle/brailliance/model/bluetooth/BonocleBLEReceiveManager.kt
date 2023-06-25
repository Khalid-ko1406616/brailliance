package com.bonocle.brailliance.model.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.*


//USED WITH DAGGER-HILT INJECTIONS IN PROOF OF CONCEPT PROJECT
@SuppressLint("MissingPermission")
class BonocleBLEReceiveManager(
    private val bluetoothAdapter: BluetoothAdapter,
    private val context: Context
) : BonocleReceiveManager {

    private val DEVICE_NAME = "Bonocle"
    private val FEEDBACK_SERVICE_UUID = "0000a000-0000-1000-8000-00805f9b34fb"
    private val WRITE_SERVICE_UUID = "0000b000-0000-1000-8000-00805f9b34fb"
    private val BUTTON_CHARACTERISITICS_UUID = "0000a002-0000-1000-8000-00805f9b34fb"
    private val PINS_CHARACTERISITICS_UUID = "0000b003-0000-1000-8000-00805f9b34fb"


    override val data: MutableSharedFlow<Resource<BonocleResult>> = MutableSharedFlow()


    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private var gatt: BluetoothGatt? = null

    private var isScanning = false

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val scanCallback = object : ScanCallback(){
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if(result.device.name == DEVICE_NAME){
                coroutineScope.launch {
                    data.emit(Resource.Loading(message = "Connecting to device..."))
                }
                if(isScanning){
                    result.device.connectGatt(context,false,gattCallback)
                    isScanning = false
                    bleScanner.stopScan(this)
                }
            }
        }
    }

    private var currentConnectionAttempt = 1
    private var MAXIMUM_CONNECTION_ATTEMPTS = 5

    private val gattCallback = object : BluetoothGattCallback(){
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                if(newState == BluetoothProfile.STATE_CONNECTED){
                    coroutineScope.launch {
                        data.emit(Resource.Loading(message = "Discovering Services..."))
                    }
                    gatt.discoverServices()
                    this@BonocleBLEReceiveManager.gatt = gatt
                }
            }else if (newState == BluetoothProfile.STATE_DISCONNECTED){
                coroutineScope.launch {
                    data.emit(Resource.Success(data= BonocleResult(0,ConnectionState.Disconnected)))
                }
                gatt.close()
            }else{
                gatt.close()
                currentConnectionAttempt += 1
                coroutineScope.launch {
                    data.emit(
                        Resource.Loading(
                            message = "Attempting to connect $currentConnectionAttempt/$MAXIMUM_CONNECTION_ATTEMPTS"
                        )
                    )
                }
                if(currentConnectionAttempt<= MAXIMUM_CONNECTION_ATTEMPTS){
                    startReceiving()
                }else{
                    coroutineScope.launch {
                        data.emit(Resource.Error(errorMessage = "Could not connect to ble device"))
                    }
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            with(gatt){
                printGattTable()
                coroutineScope.launch {
                    data.emit(Resource.Loading(message = "Adjusting MTU space..."))
                }
                gatt.requestMtu(517)//BYTES
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            val characteristic = findCharacteristic(FEEDBACK_SERVICE_UUID,BUTTON_CHARACTERISITICS_UUID)
            if(characteristic == null){
                coroutineScope.launch {
                    data.emit(Resource.Error(errorMessage = "Could not find bonocle publisher"))
                }
                return
            }
            enableNotification(characteristic)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {

            if(characteristic!!.uuid == UUID.fromString(BUTTON_CHARACTERISITICS_UUID)){
                //XXX X X X X
                val buttonNumber = characteristic.value[0].toInt().minus(7)
//                        val buttonNumber = value[0].toInt() + value[1].toInt() + value[2].toInt()
//                        val isPressed = value[4].toInt()
                val bonocleResult = BonocleResult(
                    buttonNumber,
                    ConnectionState.Connected
                )
                coroutineScope.launch {
                    data.emit(
                        Resource.Success(data = bonocleResult)
                    )
                }
            }
//            else if(characteristic!!.uuid == UUID.fromString(SOMETHING_UUID)){
//                val bonocleResult = BonocleResult(
//                    characteristic.value[0].toInt(),
//                    ConnectionState.Connected
//                )
//                coroutineScope.launch {
//                    data.emit(
//                        Resource.Success(data = bonocleResult)
//                    )
//                }
//            }

//            with(characteristic){
//                when(uuid){
//                    UUID.fromString(BONOCLE_BUTTON_CHARACTERISITICS_UUID) -> {
//                        //XXX X X X X
//                        val buttonNumber = value[0].toInt().minus(7)
////                        val buttonNumber = value[0].toInt() + value[1].toInt() + value[2].toInt()
////                        val isPressed = value[4].toInt()
//                        val bonocleResult = BonocleResult(
//                            buttonNumber,
//                            ConnectionState.Connected
//                        )
//                        coroutineScope.launch {
//                            data.emit(
//                                Resource.Success(data = bonocleResult)
//                            )
//                        }
//                    }
//                    else -> Unit
//                }
//            }
        }

        override fun onCharacteristicRead( //TODO
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {//CHECK IF READABLE THEN READ
//            super.onCharacteristicRead(gatt, characteristic, status)
//            characteristic?.value
        }

        override fun onCharacteristicWrite(//TODO
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {//CHECK IF WRITABLE THEN WRITE
            super.onCharacteristicWrite(gatt, characteristic, status)

        }

    }
    override fun sendPin(payload: ByteArray) {
        val characteristic = gatt?.getService(UUID.fromString(WRITE_SERVICE_UUID))?.getCharacteristic(UUID.fromString(PINS_CHARACTERISITICS_UUID))

        val writeType = when{
            characteristic!!.isWritable() -> BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
            characteristic.isWritableWithoutResponse() -> BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
            else -> error("Characteristic ${characteristic.uuid} cannot be written to")
        }

        gatt?.let { g ->
            characteristic.writeType = writeType
            characteristic.value = payload
            g.writeCharacteristic(characteristic)
        } ?: error("Not connected to a BLE device!")
    }

    private fun example(){

        val characteristic = gatt?.getService(UUID.fromString("EXAMPLE"))?.getCharacteristic(UUID.fromString("EXAMPLE"))
        gatt?.readCharacteristic(characteristic)

    }

    private fun enableNotification(characteristic: BluetoothGattCharacteristic){
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        val payload = when{
            characteristic.isIndicatable() -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            characteristic.isNotifiable() -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            else -> return
        }

        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
            if(gatt?.setCharacteristicNotification(characteristic,true) == false){
                Log.d("BLEReceiveManager","Set characteristics notification failed")
                return
            }

            writeDescription(cccdDescriptor, payload)

        }
    }

    private fun writeDescription(descriptor: BluetoothGattDescriptor, payload: ByteArray){
        gatt?.let { gatt ->
            descriptor.value = payload
            gatt.writeDescriptor(descriptor)
        } ?: error("Not connected to a BLE device!")
    }

    private fun findCharacteristic(serviceUUID: String, characteristicsUUID: String):BluetoothGattCharacteristic?{
        return gatt?.services?.find{ service ->
            service.uuid.toString() == serviceUUID
        }?.characteristics?.find { characteristics ->
            characteristics.uuid.toString() == characteristicsUUID
        }
    }


    override fun startReceiving() {
        coroutineScope.launch {
            data.emit(Resource.Loading(message = "Scanning Ble devices..."))
        }
        isScanning = true
        bleScanner.startScan(null,scanSettings,scanCallback)
    }

    override fun reconnect() {
        gatt?.connect()
    }

    override fun disconnect() {
        gatt?.disconnect()
    }


    override fun closeConnection() {
        bleScanner.stopScan(scanCallback)
        var characteristic = findCharacteristic(FEEDBACK_SERVICE_UUID,BUTTON_CHARACTERISITICS_UUID)
        if(characteristic != null){
            disconnectCharacteristic(characteristic)
        }
        characteristic = findCharacteristic(FEEDBACK_SERVICE_UUID,PINS_CHARACTERISITICS_UUID)
        if(characteristic != null){
            disconnectCharacteristic(characteristic)
        }
        gatt?.close()
    }

    private fun disconnectCharacteristic(characteristic: BluetoothGattCharacteristic){
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        characteristic.getDescriptor(cccdUuid)?.let{ cccdDescriptor ->
            if(gatt?.setCharacteristicNotification(characteristic,false) == false){
                Log.d("BLEReceiveManager","set characteristics notification failed")
                return
            }
            writeDescription(cccdDescriptor,BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
        }
    }

    fun String.decodeHex(): ByteArray {
        check(length % 2 == 0) {error("Must have an even length")}

        return chunked(2)
            .map {it.toInt(16).toByte()}
            .toByteArray()
    }

}