package gawquon.mapletherm.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapletherm.R
import gawquon.mapletherm.core.viewmodel.TemperatureViewModel
import gawquon.mapletherm.ui.getFontSize
import gawquon.mapletherm.ui.misc.KeepScreenOn
import gawquon.mapletherm.ui.misc.addUnits

@Composable
fun TemperatureScreen(
    temperatureViewModel: TemperatureViewModel = viewModel(),
    deviceAddress: String?
) {
    val temperatureUiState by temperatureViewModel.uiState.collectAsState()
    val orientation = LocalContext.current.resources.configuration.orientation

    if (deviceAddress == null) {
        throw Exception("Device address from NavHost is null")
    }
    temperatureViewModel.connectToThermometer(deviceAddress.toString(), LocalContext.current)
    KeepScreenOn()

    val fontSize = getFontSize(22, 13, orientation)
    val text = if (temperatureUiState.temperatureF < 32) {
        "Getting ".addUnits(stringResource(R.string.temp_f))
    } else {
        temperatureUiState.temperatureF.toString().addUnits(stringResource(R.string.temp_f))
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
            fontSize = fontSize
        )
    }
}