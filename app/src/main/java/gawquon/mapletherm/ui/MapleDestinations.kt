package gawquon.mapletherm.ui

import androidx.compose.runtime.Composable

interface MapleDestination {
    val route: String
}

object Connection : MapleDestination {
    override val route = "ble_connections"
}

object Thermometer : MapleDestination {
    override val route = "thermometer"
}