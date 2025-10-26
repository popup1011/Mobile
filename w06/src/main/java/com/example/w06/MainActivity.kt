package com.example.w06

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BubbleGameScreen()
                }
            }
        }
    }
}

// --- 1️⃣ 버블 모양 타입 ---
enum class BubbleShape { Circle, Star, Heart }

// --- 2️⃣ 버블 데이터 클래스 ---
data class Bubble(
    val id: Int,
    val position: Offset,
    val radius: Float,
    val color: Color,
    val shapeType: BubbleShape = BubbleShape.Circle,
    val creationTime: Long = System.currentTimeMillis(),
    val velocityX: Float = Random.nextFloat() * 8 - 4,
    val velocityY: Float = Random.nextFloat() * 8 - 4
)

// --- 3️⃣ 게임 상태 클래스 ---
class GameState(initialBubbles: List<Bubble> = emptyList()) {
    var bubbles by mutableStateOf(initialBubbles)
    var score by mutableStateOf(0)
    var isGameOver by mutableStateOf(false)
    var timeLeft by mutableStateOf(60)
}

// --- 4️⃣ 게임 화면 ---
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BubbleGameScreen() {
    val gameState: GameState = remember { GameState() }

    // 타이머
    LaunchedEffect(gameState.isGameOver) {
        if (!gameState.isGameOver && gameState.timeLeft > 0) {
            while (true) {
                delay(1000L)
                gameState.timeLeft--
                if (gameState.timeLeft == 0) {
                    gameState.isGameOver = true
                    break
                }
                val currentTime = System.currentTimeMillis()
                gameState.bubbles = gameState.bubbles.filter {
                    currentTime - it.creationTime < 3000
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GameStatusRow(score = gameState.score, timeLeft = gameState.timeLeft)

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current
            val canvasWidthPx = with(density) { maxWidth.toPx() }
            val canvasHeightPx = with(density) { maxHeight.toPx() }

            LaunchedEffect(gameState.isGameOver) {
                if (!gameState.isGameOver) {
                    while (true) {
                        delay(16)

                        // 버블이 없으면 3개 생성
                        if (gameState.bubbles.isEmpty()) {
                            val newBubbles = List(3) {
                                Bubble(
                                    id = Random.nextInt(),
                                    position = Offset(
                                        x = Random.nextFloat() * maxWidth.value,
                                        y = Random.nextFloat() * maxHeight.value
                                    ),
                                    radius = Random.nextFloat() * 25 + 25,
                                    color = Color(
                                        Random.nextInt(256),
                                        Random.nextInt(256),
                                        Random.nextInt(256),
                                        200
                                    ),
                                    shapeType = BubbleShape.values().random()
                                )
                            }
                            gameState.bubbles = newBubbles
                        }

                        // 새 버블 랜덤 생성
                        if (Random.nextFloat() < 0.05f && gameState.bubbles.size < 15) {
                            val newBubble = Bubble(
                                id = Random.nextInt(),
                                position = Offset(
                                    x = Random.nextFloat() * maxWidth.value,
                                    y = Random.nextFloat() * maxHeight.value
                                ),
                                radius = Random.nextFloat() * 50 + 50,
                                color = Color(
                                    red = Random.nextInt(256),
                                    green = Random.nextInt(256),
                                    blue = Random.nextInt(256),
                                    alpha = 200
                                ),
                                shapeType = BubbleShape.values().random()
                            )
                            gameState.bubbles = gameState.bubbles + newBubble
                        }

                        // 버블 이동
                        gameState.bubbles = gameState.bubbles.map { bubble ->
                            with(density) {
                                val radiusPx = bubble.radius.dp.toPx()
                                var xPx = bubble.position.x.dp.toPx()
                                var yPx = bubble.position.y.dp.toPx()
                                val vxPx = bubble.velocityX.dp.toPx()
                                val vyPx = bubble.velocityY.dp.toPx()

                                xPx += vxPx
                                yPx += vyPx

                                var newVx = bubble.velocityX
                                var newVy = bubble.velocityY

                                if (xPx < radiusPx || xPx > canvasWidthPx - radiusPx) newVx *= -1
                                if (yPx < radiusPx || yPx > canvasHeightPx - radiusPx) newVy *= -1

                                xPx = xPx.coerceIn(radiusPx, canvasWidthPx - radiusPx)
                                yPx = yPx.coerceIn(radiusPx, canvasHeightPx - radiusPx)

                                bubble.copy(
                                    position = Offset(xPx.toDp().value, yPx.toDp().value),
                                    velocityX = newVx,
                                    velocityY = newVy
                                )
                            }
                        }
                    }
                }
            }

            // 버블 그리기
            gameState.bubbles.forEach { bubble ->
                BubbleComposable(bubble = bubble) {
                    gameState.score++
                    gameState.bubbles = gameState.bubbles.filterNot { it.id == bubble.id }
                }
            }
        }
    }
}

// --- 5️⃣ BubbleComposable ---
@Composable
fun BubbleComposable(bubble: Bubble, onClick: () -> Unit) {
    val density = LocalDensity.current
    Canvas(
        modifier = Modifier
            .offset(x = bubble.position.x.dp, y = bubble.position.y.dp)
            .size((bubble.radius * 2).dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        when (bubble.shapeType) {
            BubbleShape.Circle -> drawCircle(
                color = bubble.color,
                radius = size.width / 2,
                center = center
            )
            BubbleShape.Star -> drawPath(createStarPath(size), color = bubble.color)
            BubbleShape.Heart -> drawPath(createHeartPath(size), color = bubble.color)
        }
    }
}

// --- 6️⃣ Star Path ---
fun createStarPath(size: androidx.compose.ui.geometry.Size): Path {
    val path = Path()
    val midX = size.width / 2
    val midY = size.height / 2
    val radius = size.width / 2
    for (i in 0 until 5) {
        val angle = Math.toRadians((i * 144).toDouble())
        val x = midX + radius * Math.cos(angle).toFloat()
        val y = midY + radius * Math.sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

// --- 7️⃣ Heart Path ---
fun createHeartPath(size: androidx.compose.ui.geometry.Size): Path {
    val path = Path()
    val width = size.width
    val height = size.height
    path.moveTo(width / 2, height * 0.8f)
    path.cubicTo(
        width * 1.2f, height * 0.35f,
        width * 0.8f, -height * 0.2f,
        width / 2, height * 0.2f
    )
    path.cubicTo(
        width * 0.2f, -height * 0.2f,
        -width * 0.2f, height * 0.35f,
        width / 2, height * 0.8f
    )
    path.close()
    return path
}

// --- 8️⃣ 상단 상태 UI ---
@Composable
fun GameStatusRow(score: Int , timeLeft: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Score: $score", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "Time: ${timeLeft}s", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

// --- 9️⃣ Preview ---
@Preview(showBackground = true)
@Composable
fun BubbleGamePreview() {
    MaterialTheme {
        BubbleGameScreen()
    }
}
