package gawquon.mapletherm.core.network.bluetooth

import android.content.Context

class BluetoothLeHub(context: Context) {
    private val _bluetoothLeScanner = BluetoothLeScanner(context)
    private val _bluetoothLeReader = BluetoothLeReader(_bluetoothLeScanner.bluetoothLeManager)

    init {
        _bluetoothLeScanner.requestBt(context)
    }
}