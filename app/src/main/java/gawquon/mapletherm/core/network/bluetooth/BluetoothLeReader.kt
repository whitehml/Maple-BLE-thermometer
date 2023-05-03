package gawquon.mapletherm.core.network.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import gawquon.mapletherm.core.data.CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID
import gawquon.mapletherm.core.data.MsgTypes
import gawquon.mapletherm.core.data.fahrenheitCharacteristic
import gawquon.mapletherm.core.data.hexMap
import gawquon.mapletherm.core.viewmodel.TemperatureViewModel
import gawquon.mapletherm.ui.misc.toHexString
import java.util.UUID

private const val TAG = "BluetoothLeReader"

@Suppress("DEPRECATION")
class BluetoothLeReader(manager: BluetoothManager) {
    private val _bluetoothLeAdapter = manager.adapter

    private var _bluetoothLeGatt: BluetoothGatt? = null
    private var _isConnected = false
    private var _subscribed = false

    // Message/instruction Handlers
    // Looper comes from main thread
    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == MsgTypes.CONNECTION.ordinal) {
                val connectionMsg = msg.obj as TemperatureViewModel.ConnectionMsg
                connect(connectionMsg.address, connectionMsg.context)
            }
        }
    }

    // Static handler to send messages out
    companion object {
        private var outHandler: Handler? = null
        fun setHandler(handler: Handler) {
            this.outHandler = handler
        }
    }

    init {
        TemperatureViewModel.setHandler(handler)
    }


    // Connections
    @SuppressLint("MissingPermission")
    fun connect(address: String, context: Context): Boolean {
        _bluetoothLeAdapter?.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                // connect to the GATT server on the device
                _bluetoothLeGatt = device.connectGatt(context, true, bluetoothGattCallback)
            } catch (exception: IllegalArgumentException) {
                Log.w(TAG, "Device not found with provided address.")
                return false
            }
        } ?: run {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return false
        }
        return true
    }

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED && !_isConnected) {
                // successfully connected to the GATT Server
                _isConnected = true
                // Log.d(TAG, "Connected to BLE GATT server")
                _bluetoothLeGatt?.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED && _isConnected) {
                // disconnected from the GATT Server
                _isConnected = false
                _subscribed = false
                Log.d(TAG, "Disconnected from BLE GATT server")
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if(!_subscribed)
                {
                    subscribeToNotifications()
                }
                // Log.d(TAG, "Services discovered")
            } else {
                Log.w(TAG, "onServicesDiscovered received: $status")
            }
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int
        ) {
            Log.d(TAG, "Wrote to descriptor, hopefully we get notifications now")
        }

        override fun onCharacteristicChanged( // For Android 13+
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            sendMessage(sliceHexToTemp(value.toHexString()).toDouble())
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicChanged(
            // For Android 12-
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
        ) {
            sendMessage(sliceHexToTemp(characteristic.value.toHexString()).toDouble())
        }
    }

    @SuppressLint("MissingPermission")
    private fun close() { // call in the NavHost... if we can
        _bluetoothLeGatt?.let { gatt ->
            gatt.close()
            _bluetoothLeGatt = null
        }
    }

    // Obtain temperature characteristic
    @SuppressLint("MissingPermission")
    fun subscribeToNotifications() {
        val temperatureField =
            _bluetoothLeGatt!!.findCharacteristic(fahrenheitCharacteristic)

        if (temperatureField != null) {
            setCharacteristicNotification(temperatureField, true)
            val descriptor = temperatureField.getDescriptor(
                CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID
            )
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE // Might have to do non-deprecated version as well for Android 13+
            _bluetoothLeGatt!!.writeDescriptor(descriptor)
            _subscribed = true
        } else Log.w(TAG, "Could not find temperature field on bluetooth device.")
    }

    @SuppressLint("MissingPermission")
    fun setCharacteristicNotification(
        characteristic: BluetoothGattCharacteristic,
        enabled: Boolean
    ) {
        if (!_bluetoothLeGatt?.setCharacteristicNotification(characteristic, enabled)!!) {
            Log.d(TAG, "Failed to set notification")
        }
    }


    private fun BluetoothGatt.findCharacteristic(uuid: UUID): BluetoothGattCharacteristic? {
        // from Chee Yi Ong's BLE tutorial - https://punchthrough.com/android-ble-guide/
        services?.forEach { service ->
            service.characteristics?.firstOrNull { characteristic ->
                characteristic.uuid == uuid
            }?.let { matchingCharacteristic ->
                return matchingCharacteristic
            }
        }
        return null
    }


    // Parsing the data
    private fun sliceHexToTemp(message: String): String {
        if (message.length != 22)
            return 0.0.toString()
        val hex = message.subSequence(12, 13).toString() + message.subSequence(8, 10).toString()
        return hexToF(hex).toString()
    }

    private fun hexToF(hex: String): Double {
        return (hexMap[hex[0]]!! * 16 * 16 + hexMap[hex[1]]!! * 16 + hexMap[hex[2]]!!) * 1 / 10.0
    }

    // Messages out
    private fun sendMessage(fahrenheit: Double) {
        if (outHandler == null) return

        outHandler?.obtainMessage(MsgTypes.TEMPERATURE_DATA.ordinal, fahrenheit)?.apply {
            sendToTarget()
        }
    }
}
