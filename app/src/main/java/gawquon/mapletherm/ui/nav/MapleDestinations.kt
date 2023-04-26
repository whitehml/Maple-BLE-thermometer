package gawquon.mapletherm.ui.nav

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface MapleDestination {
    val route: String
}

object Connection : MapleDestination {
    override val route = "ble_connections"
}

object Thermometer : MapleDestination {
    override val route = "thermometer"
    const val deviceAddressArg = "device_address"
    val routeWithArgs = "${route}/{${deviceAddressArg}}"
    val arguments = listOf(navArgument(deviceAddressArg) { type = NavType.StringType })
}