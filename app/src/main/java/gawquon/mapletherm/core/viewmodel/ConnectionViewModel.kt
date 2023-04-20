package gawquon.mapletherm.core.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConnectionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionUiState())
    val uiState: StateFlow<ConnectionUiState> = _uiState.asStateFlow()

    fun toggleScan() {
        _uiState.update { currentState -> currentState.copy( isScanning = !currentState.isScanning) }
    }

    fun connectToDevice() {
        // Connect to bluetooth device on button-press of the device "card"
        // Upon successful connection, Navigate ConnectionScreen to TemperatureScreen
    }
}