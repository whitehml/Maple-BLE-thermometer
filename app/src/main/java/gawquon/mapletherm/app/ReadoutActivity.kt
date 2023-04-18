package gawquon.mapletherm.app

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import gawquon.mapletherm.core.datastore.LocalDataSource
import gawquon.mapletherm.ui.theme.MapleThermTheme

class ReadoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapleThermTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }
        }
    }
}

/*@Composable
fun SensorDisplays(isSensor: Boolean) {
    val context = LocalContext.current
    val store = LocalDataSource(context)
    val pressure = store.getAccessToken.collectAsState(initial = "")
    Box {
        if (isSensor != null) {
            Text(text = pressure.value.toString())
        } else {
            Text(text = "Device lacks barometric sensor")
        }
    }
}*/
