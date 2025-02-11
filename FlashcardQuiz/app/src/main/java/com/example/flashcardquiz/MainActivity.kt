package com.example.flashcardquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcardquiz.data.FlashCard
import com.example.flashcardquiz.data.makeListOfQuestions
import com.example.flashcardquiz.ui.theme.FlashcardQuizTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashcardQuizTheme {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CardRow()
                }
            }
        }
    }
}

@Composable
fun SingleCard(card: FlashCard, modifier: Modifier = Modifier) {
    var revealed by remember { mutableStateOf(false) }
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = modifier.clickable {
            revealed = revealed.not()
        }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight(0.4f)
                .fillMaxWidth(0.8f),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(card.question, fontSize = 24.sp)
            Column(verticalArrangement = Arrangement.SpaceAround) {
                card.answers.forEachIndexed { i, answer ->
                    Text("${i+1}. $answer", fontSize = 20.sp, fontWeight = FontWeight(500), color = if (revealed && answer == card.correct) Color(10, 143, 26) else Color.Black)
                }
            }
        }
    }
}

@Composable
fun CardRow() {
    val listState = rememberLazyListState()
    val context = LocalContext.current
    var cards by remember { mutableStateOf(makeListOfQuestions(context)) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(15000)
            cards = cards.shuffled()
        }
    }

    LazyRow(state = listState) {
        items(cards) { card ->
            SingleCard(card, Modifier.padding(horizontal = 8.dp))
        }
    }
}