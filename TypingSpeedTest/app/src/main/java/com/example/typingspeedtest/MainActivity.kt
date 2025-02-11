package com.example.typingspeedtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.typingspeedtest.data.TypingViewModel
import com.example.typingspeedtest.ui.theme.TypingSpeedTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = TypingViewModel()
        viewModel.initialize(applicationContext)

        enableEdgeToEdge()
        setContent {
            TypingSpeedTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TypingSpeedTest(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TypingSpeedTest(viewModel: TypingViewModel, modifier: Modifier = Modifier) {
    val words by viewModel.words.collectAsState()
    val wpm by viewModel.wpm.collectAsState()
    var currentInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "WPM: $wpm",
            fontSize = 30.sp,
            fontWeight = FontWeight(600)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(words) { word ->
                Text(
                    text = word,
                    fontSize = 24.sp
                )
            }
        }

        TextField(
            value = currentInput,
            onValueChange = { input ->
                currentInput = input
                if (input.endsWith(" ")) {
                    viewModel.checkWord(input.trim())
                    currentInput = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Type here") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}