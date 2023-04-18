package gawquon.mapletherm.ui

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
import gawquon.mapletherm.core.viewmodel.PressureViewModel

@Composable
fun PressureScreen(pressureViewModel: PressureViewModel = viewModel()) {
    val pressureUiState by pressureViewModel.uiState.collectAsState()
    val orientation = LocalContext.current.resources.configuration.orientation

    if (orientation == PORTRAIT) {
        PressurePortrait(pressureViewModel)
    } else {
        PressureLandscape(pressureViewModel)
    }
}

@Composable
fun PressurePortrait(pressureViewModel: PressureViewModel) {
    val fontSize = 6.em

    Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
        Column() {
            Text(text = "Pressure mmHg", fontSize = fontSize)
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
fun PressureLandscape(pressureViewModel: PressureViewModel) {
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