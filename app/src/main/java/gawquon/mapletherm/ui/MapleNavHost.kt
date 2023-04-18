package gawquon.mapletherm.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavHostComp(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Connection.route) {
        composable(route = Connection.route) {
            ConnectionScreen(onClickFoundTherm = { navController.navigateSingleTopTo(Thermometer.route) })
        }
        composable(route = Thermometer.route) {
            TemperatureScreen()
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
    }