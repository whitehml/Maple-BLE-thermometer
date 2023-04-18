package gawquon.mapletherm.core.viewmodel

import androidx.lifecycle.ViewModel
import gawquon.mapletherm.core.data.ConnectionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PressureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionUiState())
    val uiState: StateFlow<ConnectionUiState> = _uiState.asStateFlow()

    private fun readPressure() {
        // start the scan process
    }
}