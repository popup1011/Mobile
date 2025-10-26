package com.example.w03_compose
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.w03_compose.ui.theme.ComposeLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeLabTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Animal")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "pea",
                )
            }
        }
    ) { innerPadding ->
        // Column: UI 요소들을 세로로 나열하는 컨테이너입니다.
        Column(
            // modifier: Composable의 크기, 패딩, 정렬 등 속성을 지정합니다.
            modifier = Modifier
                .fillMaxSize() // 화면 전체를 차지하도록 설정
                // .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(innerPadding), // Scaffold가 제공하는 패딩 적용

            // verticalArrangement: 세로 방향 정렬을 지정합니다. (여기서는 중앙)
            verticalArrangement = Arrangement.Center,

            // horizontalAlignment: 가로 방향 정렬을 지정합니다. (모든 자식 요소들이 중앙 정렬됨)
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text ="Cute dog",
                style = MaterialTheme.typography.headlineMedium // H2 스타일
            )
            Image(
                painter = painterResource(id = R.drawable.compose),
                contentDescription = "null",
                modifier = Modifier
                    .size(300.dp) // 이미지 크기 지정
                    .padding(16.dp)
            )
            Row {
                // 테마의 primary 색상이 적용된 버튼
                Button(onClick = {}) { Text("쓰다 듬기") }
                Button(onClick = {}) { Text("명령 하기") }
            }
            Text(
                modifier = Modifier.padding(8.dp),
                text ="귀여운 강아지"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DefaultScreen()
}

@Composable
fun MainScreen() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)  // 1. Scaffold의 안전 영역 패딩 먼저 적용
                .fillMaxSize(),         // 2. 남은 공간 전체 차지
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("She's name is pea")
            Image(
                painter = painterResource(id = R.drawable.compose),
                contentDescription = "null",
                modifier = Modifier
                    .size(300.dp)
                    .padding(16.dp)
            )
            Text("귀여운 강아지")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}