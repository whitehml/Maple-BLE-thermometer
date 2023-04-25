package gawquon.mapletherm.core.network.bluetooth

import android.content.Context


class BluetoothLeService(context: Context) {
    private val bluetoothLeScanner = BluetoothLeScanner(context)

    init {
        bluetoothLeScanner.requestBt(context)
    }
}