package gawquon.mapletherm.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import gawquon.mapletherm.core.sensor.PressureSensor
import gawquon.mapletherm.core.viewmodel.PressureUiState
import gawquon.mapletherm.core.viewmodel.PressureViewModel
import gawquon.mapletherm.ui.PORTRAIT

@Composable
fun PressureScreen(pressureViewModel: PressureViewModel = viewModel()) {
    val pressureUiState by pressureViewModel.uiState.collectAsState()
    val orientation = LocalContext.current.resources.configuration.orientation

    val pressureSensor = PressureSensor(context = LocalContext.current) // pass viewmodel fxn?

    if (orientation == PORTRAIT) {
        PressurePortrait(pressureUiState)
    } else {
        PressureLandscape(pressureUiState)
    }
}

@Composable
fun PressurePortrait(pressureUiState: PressureUiState) {
    val fontSize = 6.em

    Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
        Column() {
            Text(text = pressureUiState.pressureMmHg.toString(), fontSize = fontSize)
            Spacer(modifier = Modifier.weight(0.3f))
            Text(text = "Pressure atms", fontSize = fontSize)
        }
        Spacer(modifier = Modifier.weight(0.3f))
        Column() {
            Text(text = "BP Water", fontSize = fontSize)
            Spacer(modifier = Modifier.weight(0.3f))
            Text(text = "BP Syrup", fontSize = fontSize)
        }
    }
}

@Composable
fun PressureLandscape(pressureUiState: PressureUiState) {
    val fontSize = 6.em

    Column(modifier = Modifier.padding(15.dp)) {
        Text(text = "Pressure mmHg", fontSize = fontSize)
        Spacer(modifier = Modifier.weight(0.1f))
        Text(text = "Pressure atms", fontSize = fontSize)
        Spacer(modifier = Modifier.weight(0.1f))
        Text(text = "BP Water", fontSize = fontSize)
        Spacer(modifier = Modifier.weight(0.1f))
        Text(text = "BP Syrup", fontSize = fontSize)
    }
}