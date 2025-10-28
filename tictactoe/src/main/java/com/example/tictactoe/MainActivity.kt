package com.example.tictactoe

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeGame { msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun TicTacToeGame(onToast: (String) -> Unit) {
    // ❗ 2D Array 대신 List<List<String>> + mutableStateOf 사용
    var board by remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var gameOver by remember { mutableStateOf(false) }

    fun checkWin(symbol: String): Boolean {
        for (i in 0..2) {
            if ((board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) ||
                (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)
            ) return true
        }
        if ((board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
            (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)
        ) return true
        return false
    }

    fun isDraw() = board.all { row -> row.all { it.isNotEmpty() } }

    fun reset() {
        board = List(3) { MutableList(3) { "" } }
        gameOver = false
    }

    fun aiMove() {
        if (gameOver) return
        val empty = mutableListOf<Pair<Int, Int>>()
        for (r in 0..2) for (c in 0..2)
            if (board[r][c].isEmpty()) empty.add(r to c)
        if (empty.isEmpty()) return
        val (row, col) = empty[Random.nextInt(empty.size)]
        board = board.mapIndexed { r, rowList ->
            rowList.mapIndexed { c, value ->
                if (r == row && c == col) "O" else value
            }.toMutableList()
        }
        if (checkWin("O")) {
            onToast("Computer Wins!")
            gameOver = true
        } else if (isDraw()) {
            onToast("Draw!")
            gameOver = true
        }
    }

    fun playerMove(row: Int, col: Int) {
        if (board[row][col].isNotEmpty() || gameOver) return
        // ✅ 새 리스트를 만들어 상태 갱신
        board = board.mapIndexed { r, rowList ->
            rowList.mapIndexed { c, value ->
                if (r == row && c == col) "X" else value
            }.toMutableList()
        }

        if (checkWin("X")) {
            onToast("You Win!")
            gameOver = true
            return
        }
        if (isDraw()) {
            onToast("Draw!")
            gameOver = true
            return
        }

        aiMove()
    }

    // 🎨 UI 구성
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Tic Tac Toe", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        for (r in 0..2) {
            Row {
                for (c in 0..2) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                            .clickable { playerMove(r, c) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = board[r][c],
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (board[r][c]) {
                                "X" -> Color.Blue
                                "O" -> Color.Red
                                else -> Color.Transparent
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = { reset() }) {
            Text("Restart Game")
        }
    }
}
