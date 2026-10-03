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

    private fun rollDice(): Int {

        return Random.nextInt(1, 7)
    }

    fun rollWithCoroutine() {

        viewModelScope.launch {

            repeat(10) {

                diceValues = List(5) {
                    rollDice()
                }

                delay(100)
            }

            evaluateDice()
        }
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
