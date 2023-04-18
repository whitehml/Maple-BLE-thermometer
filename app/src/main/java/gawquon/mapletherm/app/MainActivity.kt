package gawquon.mapletherm.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.compose.rememberNavController
import gawquon.mapletherm.ui.Connection
import gawquon.mapletherm.ui.ConnectionScreen
import gawquon.mapletherm.ui.MapleDestination
import gawquon.mapletherm.ui.MapleScaffold
import gawquon.mapletherm.ui.TemperatureScreen
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