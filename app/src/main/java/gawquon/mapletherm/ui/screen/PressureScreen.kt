package gawquon.mapletherm.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapletherm.R
import gawquon.mapletherm.core.viewmodel.PressureUiState
import gawquon.mapletherm.core.viewmodel.PressureViewModel
import gawquon.mapletherm.ui.PORTRAIT
import gawquon.mapletherm.ui.misc.addUnits
import gawquon.mapletherm.ui.misc.format
import gawquon.mapletherm.ui.theme.BrickRed

@Composable
fun PressureScreen(pressureViewModel: PressureViewModel = viewModel()) {
    val pressureUiState by pressureViewModel.uiState.collectAsState()
    val orientation = LocalContext.current.resources.configuration.orientation

    if (orientation == PORTRAIT) {
        PressurePortrait(pressureUiState)
    } else {
        PressureLandscape(pressureUiState)
    }
}

@Composable
fun PressurePortrait(pressureUiState: PressureUiState) {
    val fontSize = 7.em

    Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
        Column() {
            Text(
                text = pressureUiState.pressureMmHg.format()
                    .addUnits(stringResource(id = R.string.pressure_mmhg)), fontSize = fontSize
            )
            Spacer(modifier = Modifier.weight(0.3f))
            Text(
                text = pressureUiState.pressureAtm.format(3)
                    .addUnits(stringResource(id = R.string.pressure_atms)), fontSize = fontSize
            )
        }
        Spacer(modifier = Modifier.weight(0.3f))
        Column() {
            Text(
                text = pressureUiState.bpWaterF.format(2)
                    .addUnits(stringResource(id = R.string.temp_f)), fontSize = fontSize
            )
            Spacer(modifier = Modifier.weight(0.3f))
            Text(
                text = pressureUiState.bpSyrupF.format(2)
                    .addUnits(stringResource(id = R.string.temp_f)), fontSize = fontSize
            )
        }
    }
}

@Composable
fun PressureLandscape(pressureUiState: PressureUiState) {
    val fontSize = 7.em

    Column(modifier = Modifier.padding(15.dp)) {
        Text(
            text = pressureUiState.pressureMmHg.format(),
            fontSize = fontSize,
            modifier = Modifier.align(
                CenterHorizontally
            )
        )
        Text(
            text = stringResource(id = R.string.pressure_mmhg),
            fontSize = fontSize,
            modifier = Modifier.align(
                CenterHorizontally
            )
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            text = pressureUiState.pressureAtm.format(3),
            fontSize = fontSize,
            modifier = Modifier.align(
                CenterHorizontally
            )
        )
        Text(
            text = stringResource(id = R.string.pressure_atms),
            fontSize = fontSize,
            modifier = Modifier.align(
                CenterHorizontally
            )
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            text = pressureUiState.bpWaterF.format(2),
            fontSize = fontSize,
            modifier = Modifier.align(
                CenterHorizontally
            )
        )
        Text(
            text = stringResource(id = R.string.temp_f),
            fontSize = fontSize,
            modifier = Modifier.align(
                CenterHorizontally
            )
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            text = pressureUiState.bpSyrupF.format(2),
            fontSize = fontSize,
            modifier = Modifier.align(
                CenterHorizontally
            )
        )
        Text(
            text = stringResource(id = R.string.temp_f),
            fontSize = fontSize,
            modifier = Modifier.align(
                CenterHorizontally
            )
        )
    }
}