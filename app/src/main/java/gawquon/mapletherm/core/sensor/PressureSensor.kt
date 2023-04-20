package gawquon.mapletherm.core.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import gawquon.mapletherm.core.data.vaporPressures

class PressureSensor(private val context: Context) : SensorEventListener {
    private var sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var mPressure: Sensor? = null

    val sensorExistsOnDevice: Boolean
        get() = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null

    init {
        mPressure = sensorManager!!.getDefaultSensor(Sensor.TYPE_PRESSURE)
        startListening()
    }


    private fun startListening() {
        if (!sensorExistsOnDevice) {
            return
        }
        sensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stopListening() { // In a single activity setup, call this when navigating away
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit // For Now

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.values.isNotEmpty()) {
            //Log.d("GWQ", event.values[0].toString())
            //Log.d("GWQ", mBarTommHg(event.values[0]).toString())
            Log.d("GWQ", getBoilingPointWater(mBarTommHg(event.values[0])).toString())
        }
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
}