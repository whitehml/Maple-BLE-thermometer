package gawquon.mapletherm.core.viewmodel

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConnectionViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionUiState())
    val uiState: StateFlow<ConnectionUiState> = _uiState.asStateFlow()

    // Sends signal
    private var outThread: HandlerThread? = null
    private var outHandler: Handler? = null

    // Listens for scan info
    companion object {
        private var handler: Handler? = null
        fun setHandler(handler: Handler) {
            this.handler = handler
        }
    }

    fun toggleScan(context: Context) {
        if(!_uiState.value.isScanning)
        {
            // start scan
                _uiState.update { currentState -> currentState.copy( isScanning = !currentState.isScanning) }
        }else {
            // stop scan
            _uiState.update { currentState -> currentState.copy( isScanning = !currentState.isScanning) }
        }

    }

    fun connectToDevice() {
        // Connect to bluetooth device on button-press of the device "card"
        // Upon successful connection, Navigate ConnectionScreen to TemperatureScreen
    }
}