package gawquon.mapletherm.core.network.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.core.app.ActivityCompat
import gawquon.mapletherm.core.viewmodel.ConnectionViewModel

class BluetoothLeScanner(context: Context) {
    private val _bluetoothLeManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val _bluetoothLeAdapter = _bluetoothLeManager.adapter
    private val _bluetoothLeScanner = _bluetoothLeAdapter.bluetoothLeScanner

    // Handler
    // Looper comes from main thread
    private val handler: Handler = object: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val scanData = msg.obj as ScanResult
            Log.d("GWQ", scanData.rssi.toString()) // Signal strength
        }
    }

    init {
        ConnectionViewModel.setHandler(handler)
    }

    // Callbacks
    private val _scanResults = mutableListOf<ScanResult>()

    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val indexQuery =
                _scanResults.indexOfFirst { it.device.address == result.device.address }
            if (indexQuery != -1) { // A scan result already exists with the same address
                _scanResults[indexQuery] = result
                // send result to Handler
            } else {
                with(result.device) {
                    Log.i(
                        "leScan",
                        "Found BLE device! Name: address: $address"
                    ) //${name ?: "Unnamed"}, address: $address")
                }
                _scanResults.add(result)
                // Send result to handler
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("onScanFailed", "code $errorCode")
        }
    }

    fun requestBt(context: Context): Unit {
        if (!_bluetoothLeAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            ActivityCompat.startActivityForResult(context as Activity, enableBtIntent, 1, null)
        }
    }

    @SuppressLint("MissingPermission")
    fun startLeScan(context: Context): Boolean { // Return true if successful
        if (checkPermissions(context)) {
            _bluetoothLeScanner.startScan(leScanCallback)
            return true
        }
        return false
    }

    @SuppressLint("MissingPermission")
    fun stopLeScan(): Unit {
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

    private fun requestLePermissions(context: Context): Unit {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf<String>(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN
            )
        } else {
            arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        ActivityCompat.requestPermissions(context as Activity, permissions, 1)
    }
}