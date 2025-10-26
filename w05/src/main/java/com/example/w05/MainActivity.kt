package com.example.w05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.w05.ui.theme.ComposeLabTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLabTheme {
                // 상태 생성
                val count = remember { mutableStateOf(0) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CounterApp(count = count)
                    Spacer(modifier = Modifier.height(32.dp))
                    StopWatchApp()
                }
            }
        }
    }
}

@Composable
fun CounterApp(count: MutableState<Int>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Count: ${count.value}",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = { count.value++ }) {
                Text("Increase")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { count.value = 0 }) {
                Text("Reset")
            }
        }
    }
}

@Composable
fun StopWatchApp() {
    var seconds by remember { mutableStateOf(15 * 60 + 22) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000)
            seconds++
        }
    }

    StopWatchScreen(
        seconds = seconds,
        isRunning = isRunning,
        onStartClick = { isRunning = true },
        onStopClick = { isRunning = false },
        onResetClick = {
            seconds = 0
            isRunning = false
        }
    )
}

@Composable
fun StopWatchScreen(
    seconds: Int,
    isRunning: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val minutes = seconds / 60
        val secondsToDisplay = seconds % 60
        val timeFormatted = String.format("%02d:%02d", minutes, secondsToDisplay)

        Text(
            text = timeFormatted,
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onStartClick, enabled = !isRunning) {
                Text("Start")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onStopClick, enabled = isRunning) {
                Text("Stop")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onResetClick) {
                Text("Reset")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterAppPreview() {
    val count = remember { mutableStateOf(0) }
    CounterApp(count = count)
}

@Preview(showBackground = true)
@Composable
fun StopWatchPreview() {
    StopWatchApp()
}
