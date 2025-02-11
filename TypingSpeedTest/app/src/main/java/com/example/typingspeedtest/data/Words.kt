package com.example.typingspeedtest.data

import android.content.res.XmlResourceParser
import com.example.typingspeedtest.R
import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun getAllWords(context: Context): List<String> {
    val words = mutableListOf<String>()

    val xmlParser = context.resources.getXml(R.xml.typingwords)

    while (xmlParser.eventType != XmlResourceParser.END_DOCUMENT) {
        if (xmlParser.eventType == XmlResourceParser.START_TAG) {
            if (xmlParser.name == "word") {
                words.add(xmlParser.nextText())
            }
        }
        xmlParser.next()
    }
    return words
}

class TypingViewModel : ViewModel() {
    private val _words = MutableStateFlow<List<String>>(emptyList())
    val words: StateFlow<List<String>> = _words
    private val _wpm = MutableStateFlow(0)
    val wpm: StateFlow<Int> = _wpm
    private var startTime: Long = 0
    private var wordsTyped = 0
    private lateinit var allWords: List<String>

    fun initialize(context: Context) {
        allWords = getAllWords(context)
        _words.value = allWords.shuffled().take(5)
        startWordTimer()
        startWpmTimer()
    }

    private fun startWordTimer() {
        viewModelScope.launch {
            while (true) {
                delay(5000)
                shuffleAllWords()
            }
        }
    }

    private fun startWpmTimer() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                updateWpm()
            }
        }
    }

    private fun updateWpm() {
        if (startTime != 0L) {
            val elapsedMinutes = (System.currentTimeMillis() - startTime) / 1000.0 / 60.0
            if (elapsedMinutes > 0) {
                _wpm.value = (wordsTyped / elapsedMinutes).toInt()
            }
        }
    }

    private fun shuffleAllWords() {
        _words.value = allWords.shuffled().take(5)
    }

    fun checkWord(input: String) {
        val currentWords = _words.value.toMutableList()
        if (currentWords.isNotEmpty() && input == currentWords[0]) {
            if (startTime == 0L) {
                startTime = System.currentTimeMillis()
            }
            wordsTyped++
            currentWords.removeAt(0)
            currentWords.add(allWords.random())
            _words.value = currentWords
        }
    }
}