package com.example.yahtzeegame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class YahtzeeViewModel : ViewModel() {

    var diceValues by mutableStateOf(
        listOf(1, 1, 1, 1, 1)
    )
        private set

    var categoryScores by mutableStateOf(
        emptyList<CategoryScore>()
    )
        private set

    var rollCount by mutableStateOf(0)
        private set

    var heldDice by mutableStateOf(
        List(5) { false }
    )
        private set

    var savedScores by mutableStateOf(
        emptyList<CategoryScore>()
    )
        private set

    var isRolling by mutableStateOf(false)
        private set

    var gameOver by mutableStateOf(false)
        private set

    val unusedCategories: List<YahtzeeCategory>
        get() = YahtzeeCategory.entries.filter { category ->
            savedScores.none { savedScore ->
                savedScore.category == category
            }
        }

    val totalScore: Int
        get() = savedScores.sumOf { it.score }

    private fun rollDice(): Int {

        return Random.nextInt(1, 7)
    }

    fun rollWithCoroutine() {

        if (isRolling || gameOver || rollCount >= 3) {
            return
        }

        rollCount++
        isRolling = true

        viewModelScope.launch {

            repeat(10) {

                diceValues = diceValues.mapIndexed { index, currentValue ->

                    if (heldDice[index]) {
                        currentValue
                    } else {
                        rollDice()
                    }
                }

                delay(100)
            }

            evaluateDice()
            isRolling = false
        }
    }

    fun toggleHold(index: Int) {

        if (
            index !in diceValues.indices ||
            isRolling ||
            gameOver ||
            rollCount !in 1..2
        ) {
            return
        }

        heldDice = heldDice.toMutableList().also { dice ->
            dice[index] = !dice[index]
        }
    }

    fun selectCategory(category: YahtzeeCategory) {

        if (
            isRolling ||
            gameOver ||
            rollCount != 3 ||
            savedScores.any { it.category == category }
        ) {
            return
        }

        val score = DiceRules.scoreFor(
            category = category,
            dice = diceValues
        )

        savedScores = savedScores + CategoryScore(
            category = category,
            score = score
        )

        if (savedScores.size == YahtzeeCategory.entries.size) {
            gameOver = true
            heldDice = List(5) { false }
            categoryScores = emptyList()
        } else {
            startNewTurn()
        }
    }

    fun resetGame() {

        savedScores = emptyList()
        gameOver = false
        startNewTurn()
    }

    private fun startNewTurn() {

        rollCount = 0
        diceValues = listOf(1, 1, 1, 1, 1)
        heldDice = List(5) { false }
        categoryScores = emptyList()
        isRolling = false
    }

    private fun evaluateDice() {

        categoryScores =
            DiceRules
                .getAvailableCategories(diceValues)
                .map { category ->

                    CategoryScore(
                        category = category,
                        score = DiceRules.scoreFor(
                            category = category,
                            dice = diceValues
                        )
                    )
                }
    }
}
