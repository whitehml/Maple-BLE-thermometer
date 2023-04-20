package gawquon.mapletherm.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import gawquon.mapletherm.ui.MapleScaffold
import gawquon.mapletherm.ui.theme.MapleThermTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapleThermApp()
        }
    }
}

@Composable
fun MapleThermApp() {
    MapleThermTheme {
        val navController = rememberNavController()

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MapleScaffold(
                orientation = LocalContext.current.resources.configuration.orientation,
                navController = navController
            )
        }
    }
}