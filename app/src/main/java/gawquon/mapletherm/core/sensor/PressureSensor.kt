package gawquon.mapletherm.core.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import gawquon.mapletherm.core.msg.MsgTypes

class PressureSensor(context: Context) : SensorEventListener {
    private var sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var mPressure: Sensor? = null

    // Thread to listen on
    private var sensorThread: HandlerThread? = null
    private var sensorHandler: Handler? = null

    // Static Handler to send messages out via
    companion object {
        private var outHandler: Handler? = null
        fun setHandler(handler: Handler) {
            this.outHandler = handler
        }
    }

    private val sensorExistsOnDevice: Boolean
        get() = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null

    init {
        mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        startListening()
    }

    fun startListening() {
        if (!sensorExistsOnDevice) {
            Log.i("PressureSenor", "No pressure sensor detected, will not register listener.")
            return
        }
        sensorThread = HandlerThread("PressureSensor", Thread.NORM_PRIORITY)
        sensorThread!!.start()
        sensorHandler = Handler(sensorThread!!.looper) // Blocks until looper is prepared
        sensorManager.registerListener(
            this,
            mPressure, 1000000, // Sample once a second
            sensorHandler
        )
    }

    fun stopListening() { // In a single activity setup, call this when navigating away
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit // For Now

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.values.isNotEmpty()) {
            // Send message to Pressure ViewModel
            sendMessage(event.values[0])
        }
    }

    private fun sendMessage(value: Float) {
        if (outHandler == null) return

        outHandler?.obtainMessage(MsgTypes.PRESSURE_DATA.ordinal, value)?.apply {
            sendToTarget()
        }
    }
}