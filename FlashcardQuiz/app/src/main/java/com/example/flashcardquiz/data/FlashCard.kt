package com.example.flashcardquiz.data

import android.content.Context
import com.example.flashcardquiz.R
import org.xmlpull.v1.XmlPullParser

data class FlashCard(
    val question: String,
    val answers: MutableList<String> = mutableListOf(),
    val correct: String
)

private class FlashcardRepository {
    fun getFlashcards(context: Context): List<FlashCard> {
        val cards = mutableListOf<FlashCard>()
        val xmlParser = context.resources.getXml(R.xml.flashcards)

        var question = ""
        var answer = ""

        while (xmlParser.eventType != XmlPullParser.END_DOCUMENT) {
            when (xmlParser.eventType) {
                XmlPullParser.START_TAG -> {
                    when(xmlParser.name) {
                        "question" -> question = xmlParser.nextText()
                        "answer" -> answer = xmlParser.nextText()
                    }
                }

                XmlPullParser.END_TAG -> {
                    when(xmlParser.name) {
                        "card" -> {
                            val card = FlashCard(question = question, correct = answer, answers = mutableListOf(answer))
                            cards.add(card)
                        }
                    }
                }
            }
            xmlParser.next()
        }
        return cards
    }
}

fun makeListOfQuestions(context: Context): List<FlashCard> {
    val cards = FlashcardRepository().getFlashcards(context = context)
    cards.forEach { card ->
        when (card.question) {
            "What year did World War II end?" -> {
                card.answers.addAll(listOf("1944", "1946", "1943"))
            }
            "What is the chemical symbol for gold?" -> {
                card.answers.addAll(listOf("Ag", "Fe", "Cu"))
            }
            "Who painted the Mona Lisa?" -> {
                card.answers.addAll(listOf("Michelangelo", "Raphael", "Vincent van Gogh"))
            }
            "What's the largest planet in our solar system?" -> {
                card.answers.addAll(listOf("Saturn", "Uranus", "Neptune"))
            }
            "What element has the atomic number 1?" -> {
                card.answers.addAll(listOf("Helium", "Oxygen", "Carbon"))
            }
            "Who composed the Ninth Symphony?" -> {
                card.answers.addAll(listOf("Mozart", "Bach", "Tchaikovsky"))
            }
            "What is the longest river in the world?" -> {
                card.answers.addAll(listOf("Amazon River", "Mississippi River", "Yangtze River"))
            }
            "Who is credited with inventing the telephone?" -> {
                card.answers.addAll(listOf("Thomas Edison", "Nikola Tesla", "Antonio Meucci"))
            }
            "What is the square root of 144?" -> {
                card.answers.addAll(listOf("10", "14", "16"))
            }
            "In which year did Christopher Columbus first reach the Americas?" -> {
                card.answers.addAll(listOf("1488", "1496", "1500"))
            }
        }
        card.answers.shuffle()
    }
    return cards
}