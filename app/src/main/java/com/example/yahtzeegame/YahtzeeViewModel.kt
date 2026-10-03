package com.example.yahtzeegame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random
import androidx.compose.foundation.layout.Row

class YahtzeeViewModel : ViewModel() {

    var diceValues by mutableStateOf(
        listOf(1, 1, 1, 1, 1)
    )
        private set

    private fun rollDice(): Int {

        return Random.nextInt(1, 7)
    }

    fun rollOnce() {

        diceValues = List(5) {
            rollDice()
        }
    }
}