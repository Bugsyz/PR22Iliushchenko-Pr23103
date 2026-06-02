package com.example.pr22_23101_fi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemoryGameViewModel : ViewModel() {

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

    private val _moves = MutableStateFlow(0)
    val moves: StateFlow<Int> = _moves.asStateFlow()

    private val _gameOver = MutableStateFlow(false)
    val gameOver: StateFlow<Boolean> = _gameOver.asStateFlow()

    private var firstSelectedCard: Card? = null
    private var isProcessing = false

    init {
        startNewGame()
    }

    fun startNewGame() {
        _moves.value = 0
        _gameOver.value = false
        firstSelectedCard = null
        isProcessing = false
        generateCards()
    }

    private fun generateCards() {
        val cardNames = List(18) { "card_$it" }
        val doubled = cardNames + cardNames
        val shuffled = doubled.shuffled()

        _cards.value = shuffled.mapIndexed { index, name ->
            Card(
                id = index,
                imageId = getImageResource(name),
                imageName = name,
                isFlipped = false,
                isMatched = false
            )
        }
    }

    private fun getImageResource(name: String): Int {
        return when (name) {
            "card_0" -> R.drawable.card_0
            "card_1" -> R.drawable.card_1
            "card_2" -> R.drawable.card_2
            "card_3" -> R.drawable.card_3
            "card_4" -> R.drawable.card_4
            "card_5" -> R.drawable.card_5
            "card_6" -> R.drawable.card_6
            "card_7" -> R.drawable.card_7
            "card_8" -> R.drawable.card_8
            "card_9" -> R.drawable.card_9
            "card_10" -> R.drawable.card_10
            "card_11" -> R.drawable.card_11
            "card_12" -> R.drawable.card_12
            "card_13" -> R.drawable.card_13
            "card_14" -> R.drawable.card_14
            "card_15" -> R.drawable.card_15
            "card_16" -> R.drawable.card_16
            "card_17" -> R.drawable.card_17
            else -> R.drawable.card_back
        }
    }

    fun onCardClicked(card: Card) {
        if (isProcessing || card.isFlipped || card.isMatched) return

        val currentCards = _cards.value.toMutableList()
        val cardIndex = currentCards.indexOfFirst { it.id == card.id }
        if (cardIndex == -1) return

        currentCards[cardIndex] = card.copy(isFlipped = true)
        _cards.value = currentCards

        if (firstSelectedCard == null) {
            firstSelectedCard = card
        } else {
            isProcessing = true
            _moves.value += 1

            if (firstSelectedCard!!.imageName == card.imageName) {
                val matchedCards = currentCards.map {
                    if (it.id == firstSelectedCard!!.id || it.id == card.id) {
                        it.copy(isMatched = true)
                    } else it
                }
                _cards.value = matchedCards
                firstSelectedCard = null
                isProcessing = false
                checkGameOver()
            } else {
                viewModelScope.launch {
                    delay(1000)
                    val revertedCards = _cards.value.map {
                        if (it.id == firstSelectedCard!!.id || it.id == card.id) {
                            it.copy(isFlipped = false)
                        } else it
                    }
                    _cards.value = revertedCards
                    firstSelectedCard = null
                    isProcessing = false
                }
            }
        }
    }

    private fun checkGameOver() {
        if (_cards.value.all { it.isMatched }) {
            _gameOver.value = true
        }
    }
}