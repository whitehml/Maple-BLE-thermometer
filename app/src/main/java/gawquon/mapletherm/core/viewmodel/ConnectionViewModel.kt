package gawquon.mapletherm.core.viewmodel

import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.lifecycle.ViewModel
import gawquon.mapletherm.core.data.MsgTypes
import gawquon.mapletherm.core.network.bluetooth.BluetoothLeScanner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConnectionViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionUiState())
    val uiState: StateFlow<ConnectionUiState> = _uiState.asStateFlow()

    // Static handler to send messages out
    companion object {
        private var outHandler: Handler? = null
        fun setHandler(handler: Handler) {
            this.outHandler = handler
        }
    }

    // Handler to read signals in
    // Looper comes from main thread
    @Suppress("UNCHECKED_CAST")
    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == MsgTypes.STOP_SCAN.ordinal) {
                stopScan()
            }
            if (msg.what == MsgTypes.DISCOVERED_DEVICES.ordinal) {
                val scannedDevices = msg.obj as List<ScanResult>
                updateDeviceList(scannedDevices)
            }
        }
    }

    init {
        BluetoothLeScanner.setHandler(handler)
    }

    fun toggleScan() {
        _uiState.update { currentState -> currentState.copy(isScanning = !currentState.isScanning) }
        sendScanMessage(_uiState.value.isScanning)
    }

    fun stopScan() {
        _uiState.update { currentState -> currentState.copy(isScanning = false) }
        sendScanMessage(false)
    }

    private fun updateDeviceList(discoveredDevices: List<ScanResult>) {
        _uiState.update { currentState -> currentState.copy(discoveredDevices = discoveredDevices) }
    }

    private fun sendScanMessage(value: Boolean) {
        if (outHandler == null) return

        outHandler?.obtainMessage(MsgTypes.SCAN_SIGNAL.ordinal, value)?.apply {
            sendToTarget()
        }
    }
}