package gawquon.mapletherm.core.viewmodel

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.ViewModel
import gawquon.mapletherm.core.data.MsgTypes
import gawquon.mapletherm.core.network.bluetooth.BluetoothLeReader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TemperatureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()

    // Static handler to send messages out
    companion object {
        private var outHandler: Handler? = null
        fun setHandler(handler: Handler) {
            this.outHandler = handler
        }
    }

    // Handler to read signals in
    // Looper comes from main thread
    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == MsgTypes.TEMPERATURE_DATA.ordinal) {
                val bleData = msg.obj as Double
                updateTemperature(bleData)
            }
        }
    }

    init {
        BluetoothLeReader.setHandler(handler)
    }

    private fun updateTemperature(temperature: Double) {
        _uiState.update { currentState -> currentState.copy(temperatureF = temperature) }
    }

    // Messages
    data class ConnectionMsg(val address: String, val context: Context) {
        val deviceAddress = address
        val appContext = context
    }

    fun connectToThermometer(address: String, context: Context) {
        sendScanMessage(ConnectionMsg(address, context))
    }

    private fun sendScanMessage(connection: ConnectionMsg) {
        if (outHandler == null) return

        outHandler?.obtainMessage(MsgTypes.CONNECTION.ordinal, connection)?.apply {
            sendToTarget()
        }
    }
}