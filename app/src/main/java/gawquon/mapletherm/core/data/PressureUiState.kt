package gawquon.mapletherm.core.data

data class PressureUiState(
    val pressureMmHg: Float = 0f,
    val pressureAtm: Float = 0f,
    val bpWaterF: Float = 212f,
    val bpSyrupF: Float = 219f
)