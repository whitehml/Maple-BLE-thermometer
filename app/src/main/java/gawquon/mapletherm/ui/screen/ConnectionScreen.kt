package gawquon.mapletherm.ui.screen

import android.bluetooth.le.ScanResult
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapletherm.R
import gawquon.mapletherm.core.viewmodel.ConnectionViewModel
import gawquon.mapletherm.ui.getFontSize
import gawquon.mapletherm.ui.theme.Fall

@Composable
fun ConnectionScreen(
    context: Context,
    connectionViewModel: ConnectionViewModel = viewModel(),
    onClickFoundTherm: () -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val connectionUiState by connectionViewModel.uiState.collectAsState()
        val orientation = context.resources.configuration.orientation

        ScanButton(
            orientation,
            isScanning = connectionUiState.isScanning,
            onScanClick = { connectionViewModel.toggleScan() })
        DiscoveredTherms(
            discoveredTherms = connectionUiState.discoveredDevices,
            orientation = orientation,
            onClickFoundTherm = onClickFoundTherm
        )
    }
}

@Composable
fun ScanButton(orientation: Int, isScanning: Boolean, onScanClick: () -> Unit) {
    val scanButtonText = getScanText(isScanning)

    Button(
        onClick = onScanClick,
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .fillMaxHeight(0.20f)
            .padding(horizontal = 5.dp)
            .padding(top = 15.dp, bottom = 10.dp),
        shape = RoundedCornerShape(30)
    ) {
        Text(
            text = scanButtonText,
            fontSize = getFontSize(6, 10, orientation)
        )
    }
}

@Composable
fun getScanText(isScanning: Boolean): String {
    if (isScanning) {
        return stringResource(R.string.end_scan)
    }
    return stringResource(R.string.start_scan)
}

@Composable
fun DiscoveredTherms(
    discoveredTherms: List<ScanResult>,
    orientation: Int,
    onClickFoundTherm: () -> Unit
) { //Placeholder Int
    LazyColumn {
        items(discoveredTherms) { discoveredTherm ->
            DiscoveredTherm(discoveredTherm, orientation, onClickFoundTherm)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveredTherm(device: ScanResult, orientation: Int, onClickFoundTherm: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Fall),
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 70.dp)
            .padding(vertical = 5.dp, horizontal = 15.dp)
            .clickable { onClickFoundTherm() },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 10.dp)) {
            Column {
                Text(text = "Placeholder Device Name", fontSize = getFontSize(7, 5, orientation))
                Text(text = "Placeholder ID", fontSize = getFontSize(6, 5, orientation))
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.decibels, 0),
                fontSize = getFontSize(9, 8, orientation)
            )
        }
    }
}
