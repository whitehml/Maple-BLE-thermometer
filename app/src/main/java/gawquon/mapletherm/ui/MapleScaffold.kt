package gawquon.mapletherm.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.navigation.NavHostController
import gawquon.mapletherm.ui.nav.NavHostComp
import gawquon.mapletherm.ui.screen.PressureScreen

const val PORTRAIT = 1
const val LANDSCAPE = 2

@Composable
fun MapleScaffold(
    orientation: Int,
    navController: NavHostController
) {
    val sensorManager =
        LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val hasPressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null

    if (orientation == PORTRAIT) {
        PortraitScaffold(hasPressureSensor, navController = navController)
    } else {
        LandscapeScaffold(hasPressureSensor, navController = navController)
    }
}

@Composable
fun PortraitScaffold(hasPressureSensor: Boolean, navController: NavHostController) {
    Column {
        val width = setWidthIfSensor(0.8f, hasPressureSensor)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(width)
        ) {
            NavHostComp(navController = navController)
        }
        if (hasPressureSensor) {
            Box(modifier = Modifier.fillMaxSize()) {
                PressureScreen()
            }
        }
    }
}

@Composable
fun LandscapeScaffold(hasPressureSensor: Boolean, navController: NavHostController) {
    Row {
        val width = setWidthIfSensor(0.8f, hasPressureSensor)
        Box(
            modifier = Modifier
                .fillMaxWidth(width)
                .fillMaxHeight()
        ) {
            NavHostComp(navController = navController)
        }
        if (hasPressureSensor) {
            Box(modifier = Modifier.fillMaxSize()) {
                PressureScreen()
            }
        }
    }
}

fun setWidthIfSensor(width: Float, hasPressureSensor: Boolean): Float {
    if (hasPressureSensor) {
        return width
    }
    return 1.0f
}

fun getFontSize(land: Int, port: Int, orientation: Int): TextUnit {
    if (orientation == PORTRAIT) {
        return port.em
    }
    return land.em
}