package com.example.mapletherm

import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.mapletherm.ui.theme.MapleThermTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapleThermTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Layout(resources.configuration.orientation)
                }
            }
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun Layout(orientation: Int)
{
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ScanButton(orientation)
    }
}

@Composable
fun ScanButton(orientation: Int) {
    Button(onClick =  {
        // on-click code here, but first set up the activities
    },
    modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.20f)
        .padding(horizontal = 5.dp)
        .padding(top = 15.dp, bottom = 10.dp),
    shape = RoundedCornerShape(30)
    ) {
        var fontSize = 10.em
        if (orientation == 2) {
            fontSize = 7.em
        }
        Text(text = "Start Scanning", fontSize = fontSize, color = Color.White)
    }
}