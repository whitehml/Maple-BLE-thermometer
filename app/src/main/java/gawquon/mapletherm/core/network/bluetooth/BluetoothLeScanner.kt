package gawquon.mapletherm.core.network.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.core.app.ActivityCompat
import gawquon.mapletherm.core.data.MsgTypes
import gawquon.mapletherm.core.viewmodel.ConnectionViewModel

class BluetoothLeScanner(context: Context) {
    val bluetoothLeManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val _bluetoothLeAdapter = bluetoothLeManager.adapter
    private val _bluetoothLeScanner = _bluetoothLeAdapter.bluetoothLeScanner

    // Static handler to send messages out
    companion object {
        private var outHandler: Handler? = null
        fun setHandler(handler: Handler) {
            this.outHandler = handler
        }
    }

    // Handler
    // Looper comes from main thread
    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val scanSignal = msg.obj as Boolean
            if (scanSignal) {
                startLeScan(context)
            } else {
                stopLeScan()
            }
        }
    }

    init {
        ConnectionViewModel.setHandler(handler)
    }

    // Callbacks
    private val _scanResults = mutableListOf<ScanResult>()

    private val scanFilters = listOf<ScanFilter>(
        ScanFilter.Builder().setDeviceName("MAVERICK").build()
    ) // Very specific to my case

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private val leScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val indexQuery =
                _scanResults.indexOfFirst { it.device.address == result.device.address }
            if (indexQuery != -1) { // A scan result already exists with the same address
                _scanResults[indexQuery] = result
                // Send results to handler
                sendKnownDevicesMessage(_scanResults)
            } else {
                with(result.device) {
                    Log.i(
                        "leScan",
                        "Found BLE device! Name: ${name ?: "Unnamed"} address: $address"
                    ) //${name ?: "Unnamed"}, address: $address")
                }
                _scanResults.add(result)
                // Send results to handler
                sendKnownDevicesMessage(_scanResults)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("onScanFailed", "code $errorCode")
        }
    }

    fun requestBt(context: Context) {
        if (!_bluetoothLeAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            ActivityCompat.startActivityForResult(context as Activity, enableBtIntent, 1, null)
        }
    }

    @SuppressLint("MissingPermission")
    fun startLeScan(context: Context) {
        if (checkPermissions(context)) {
            _bluetoothLeScanner.startScan(scanFilters, scanSettings, leScanCallback)
            return
        }
        sendStopMessage()
    }

    @SuppressLint("MissingPermission")
    fun stopLeScan() {
        _bluetoothLeScanner.stopScan(leScanCallback)
    }


    // Permissions
    private fun checkPermissions(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if ((context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)) {
                requestLePermissions(context)
                return false
            }
            if ((context.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)) {
                requestLePermissions(context)
                return false
            }
        } else {
            if ((context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                requestLePermissions(context)
                return false
            }
        }

        return true
    }

    private fun requestLePermissions(context: Context) {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN
            )
        } else {
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        ActivityCompat.requestPermissions(context as Activity, permissions, 1)
    }

    private fun sendStopMessage() {
        if (outHandler == null) return

        outHandler?.obtainMessage(MsgTypes.STOP_SCAN.ordinal)?.apply {
            sendToTarget()
        }
    }

    private fun sendKnownDevicesMessage(devices: List<ScanResult>) {
        if (outHandler == null) return

        outHandler?.obtainMessage(MsgTypes.DISCOVERED_DEVICES.ordinal, devices)?.apply {
            sendToTarget()
        }
    }
}