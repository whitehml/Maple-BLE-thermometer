package gawquon.mapletherm.ui.misc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView


// From stack-exchange user Fire-Zenk: https://stackoverflow.com/questions/69039723/is-there-a-jetpack-compose-equivalent-for-androidkeepscreenon-to-keep-screen-al
@Composable
fun KeepScreenOn() {
    val currentView = LocalView.current
    DisposableEffect(Unit) {
        currentView.keepScreenOn = true
        onDispose {
            currentView.keepScreenOn = false
        }
    }
}