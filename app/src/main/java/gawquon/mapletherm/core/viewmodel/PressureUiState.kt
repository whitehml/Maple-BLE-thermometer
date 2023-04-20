package gawquon.mapletherm.core.viewmodel

data class PressureUiState(
    val pressureMillibar: Float = 0f,
    val pressureMmHg: Float = 0f,
    val pressureAtm: Float = 0f,
    val bpWaterF: Float = 212f,
    val bpSyrupF: Float = 219f
)