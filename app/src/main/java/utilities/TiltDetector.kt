package utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.three_lane_road.MainActivity
import logic.GameController

class TiltDetector(context: Context, private val tiltCallback: GameController) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor
    private var currentLane = tiltCallback.getCurrLane()
    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event == null) return
            val xValue = event.values[0]

            val newLane = when {
                xValue > 4 -> 0
                xValue > 2 -> 1
                xValue in -2.0..2.0 -> 2
                xValue > -4 -> 3
                else -> 4
            }

            // Trigger lane change if it’s different from the current lane
            if (newLane != currentLane) {
                currentLane = newLane
                when (currentLane) {
                    0, 1 -> tiltCallback.tiltLeft()
                    2 -> tiltCallback.tiltCenter()
                    3, 4 -> tiltCallback.tiltRight()
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // pass
        }
    }

    fun start(){
        sensorManager
            .registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
    }



    fun stop(){
        sensorManager
            .unregisterListener(
                sensorEventListener,
                sensor
            )
    }
}