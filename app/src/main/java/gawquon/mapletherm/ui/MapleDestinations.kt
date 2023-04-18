package gawquon.mapletherm.ui

import androidx.compose.runtime.Composable

interface MapleDestination {
    val route: String
    val screen: @Composable () -> Unit
}

object Connection : MapleDestination {
    override val route = "ble_connections"
    override val screen: @Composable () -> Unit = { ConnectionScreen() }
}

object Thermomter : MapleDestination {
    override val route = "thermometer"
    override val screen: @Composable () -> Unit = { TemperatureScreen() }
}