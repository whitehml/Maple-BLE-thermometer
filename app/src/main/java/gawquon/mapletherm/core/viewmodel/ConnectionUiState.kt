package gawquon.mapletherm.core.viewmodel

import android.bluetooth.le.ScanResult

data class ConnectionUiState(
    val isScanning: Boolean = false,
    val discoveredDevices: List<ScanResult> = listOf()
)