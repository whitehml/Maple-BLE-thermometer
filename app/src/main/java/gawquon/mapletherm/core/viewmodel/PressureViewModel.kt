package gawquon.mapletherm.core.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.lifecycle.ViewModel
import gawquon.mapletherm.core.data.vaporPressures
import gawquon.mapletherm.core.sensor.PressureSensor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PressureViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(PressureUiState())
    val uiState: StateFlow<PressureUiState> = _uiState.asStateFlow()

    // Looper comes from main thread
    private val handler: Handler = object: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val sensorData = msg.obj as Float
            // Log.d("GWQ", sensorData.toString())
            calcPressureDerivatives(sensorData)
        }
    }

    init {
        PressureSensor.setHandler(handler)
    }

    private fun mBarTommHg(mBar: Float): Float {
        return mBar * 0.750062f
    }

    private fun mBarToAtm(mBar: Float): Float {
        return mBar / 1013.25f
    }

    private fun getBoilingPointWater(mmHg: Float): Float {
        val keys = vaporPressures.keys.toList().sorted()
        val (lowP, highP) = getAdjacentPressures(keys, mmHg)

        // Get temperatures in Celsius
        val lowT = vaporPressures[lowP]!!
        val highT = vaporPressures[highP]!!

        // Interpolate
        val bpT = ((mmHg - lowP) / (highP - lowP)) * (highT - lowT) + lowT
        return degCtoF(bpT)
    }

    private fun getBoilingPointSyrup(bpWater: Float): Float {
        return bpWater + 7
    }

    private fun degCtoF(c: Float): Float {
        return c * (9f / 5f) + 32
    }

    private fun getAdjacentPressures(pressures: List<Float>, target: Float): Pair<Float, Float> {
        // Quick and dirty since I'm not used to kotlin, be nice to use a set/binary
        var low = pressures.first()
        var high = pressures.last()

        for (pressure in pressures) {
            if (pressure < target) {
                low = pressure
            }
            if (pressure > target) {
                high = pressure
                break
            }
        }
        return Pair(low, high)
    }

    private fun calcPressureDerivatives(sensorValue: Float) {
        _uiState.update { currentState ->
            currentState.copy(
                pressureMillibar = sensorValue,
                pressureMmHg = mBarTommHg(sensorValue),
                pressureAtm = mBarToAtm(sensorValue),
            )
        }
        _uiState.update { currentState -> currentState.copy(bpWaterF = getBoilingPointWater(_uiState.value.pressureMmHg)) }
        _uiState.update { currentState -> currentState.copy(bpSyrupF = getBoilingPointSyrup(_uiState.value.bpWaterF)) }
    }
}