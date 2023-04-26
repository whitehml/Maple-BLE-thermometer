package gawquon.mapletherm.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import gawquon.mapletherm.ui.screen.ConnectionScreen
import gawquon.mapletherm.ui.screen.TemperatureScreen

@Composable
fun NavHostComp(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Connection.route) {
        composable(route = Connection.route) {
            ConnectionScreen(
                context = LocalContext.current,
                onClickFoundTherm = { address -> navController.navigateToThermometer(address) })
        }
        composable(
            route = Thermometer.routeWithArgs,
            arguments = Thermometer.arguments
        ) { navBackStackEntry ->
            val deviceAddress = navBackStackEntry.arguments?.getString(Thermometer.deviceAddressArg)
            TemperatureScreen(deviceAddress = deviceAddress)
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
    }
}
fun NavHostController.navigateToThermometer(address: String) {
    this.navigateSingleTopTo("${Thermometer.route}/$address")
}