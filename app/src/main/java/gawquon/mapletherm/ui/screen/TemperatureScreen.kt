package gawquon.mapletherm.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import gawquon.mapletherm.core.viewmodel.ConnectionViewModel
import gawquon.mapletherm.core.viewmodel.TemperatureViewModel

@Composable
fun TemperatureScreen(temperatureViewModel: TemperatureViewModel = viewModel()) {
    val connectionUiState by temperatureViewModel.uiState.collectAsState()
    val orientation = LocalContext.current.resources.configuration.orientation

    val fontSize = getFontSize(24, 13, orientation)
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Getting deg. F",
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
            fontSize = fontSize
        )
    }
}