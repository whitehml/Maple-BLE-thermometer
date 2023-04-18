package gawquon.mapletherm.app

import androidx.compose.runtime.Composable


@Composable
fun NavHost() {
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
