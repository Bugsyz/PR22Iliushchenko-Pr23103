package com.example.pr22_23101_fi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MemoryGameScreen()
            }
        }
    }
}

@Composable
fun MemoryGameScreen(viewModel: MemoryGameViewModel = viewModel()) {
    val cards by viewModel.cards.collectAsState()
    val moves by viewModel.moves.collectAsState()
    val gameOver by viewModel.gameOver.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFE3F2FD)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Игра Мемори",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "Ходы: $moves",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(cards) { card ->
                CardItem(
                    card = card,
                    onClick = { viewModel.onCardClicked(card) }
                )
            }
        }

        Button(
            onClick = { viewModel.startNewGame() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Новая игра")
        }

        if (gameOver) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Поздравляем!") },
                text = { Text("Вы завершили игру за $moves ходов!") },
                confirmButton = {
                    Button(onClick = { viewModel.startNewGame() }) {
                        Text("Играть снова")
                    }
                }
            )
        }
    }
}

@Composable
fun CardItem(card: Card, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(enabled = !card.isMatched && !card.isFlipped, onClick = onClick)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        if (card.isMatched || card.isFlipped) {
            Image(
                painter = painterResource(id = card.imageId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.card_back),
                contentDescription = "Закрытая карта",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}