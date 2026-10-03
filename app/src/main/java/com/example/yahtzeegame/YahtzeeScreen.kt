package com.example.yahtzeegame

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun YahtzeeScreen(
    viewModel: YahtzeeViewModel
) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Yahtzee"
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            viewModel.diceValues.forEachIndexed { index, value ->

                Die(
                    value = value,
                    isHeld = viewModel.heldDice[index],
                    canHold = viewModel.rollCount in 1..2 &&
                            !viewModel.isRolling &&
                            !viewModel.gameOver,
                    onToggleHold = {
                        viewModel.toggleHold(index)
                    }
                )
            }
        }

        if (!viewModel.gameOver) {

            Text(
                text = "Rolls: ${viewModel.rollCount}/3"
            )

            Button(
                onClick = {
                    viewModel.rollWithCoroutine()
                },
                enabled = !viewModel.isRolling &&
                        viewModel.rollCount < 3
            ) {

                Text(
                    text = when {
                        viewModel.isRolling -> "Rolling..."
                        viewModel.rollCount < 3 -> "Roll ${viewModel.rollCount + 1}"
                        else -> "3 Rolls Complete"
                    }
                )
            }

            if (
                viewModel.rollCount in 1..2 &&
                !viewModel.isRolling
            ) {
                Text(
                    text = "Tap a die to hold or release it before the next roll."
                )
            }

            if (viewModel.categoryScores.isNotEmpty()) {

                Text(
                    text = "Current combinations"
                )

                viewModel.categoryScores.forEach { result ->

                    Text(
                        text = "${result.category.displayName()}: ${result.score}"
                    )
                }
            }

            if (
                viewModel.rollCount == 3 &&
                !viewModel.isRolling
            ) {

                Text(
                    text = "Choose a category"
                )

                viewModel.unusedCategories.forEach { category ->

                    val score = DiceRules.scoreFor(
                        category = category,
                        dice = viewModel.diceValues
                    )

                    Button(
                        onClick = {
                            viewModel.selectCategory(category)
                        }
                    ) {
                        Text(
                            text = "${category.displayName()}: $score"
                        )
                    }
                }
            }
        }

        if (viewModel.savedScores.isNotEmpty()) {

            Text(
                text = "Scorecard"
            )

            viewModel.savedScores.forEach { result ->

                Text(
                    text = "${result.category.displayName()}: ${result.score}"
                )
            }

            Text(
                text = "Total Score: ${viewModel.totalScore}"
            )
        }

        if (viewModel.gameOver) {

            Text(
                text = "Game Over"
            )

            Text(
                text = "Final Score: ${viewModel.totalScore}"
            )

            Button(
                onClick = {
                    viewModel.resetGame()
                }
            ) {
                Text("New Game")
            }
        }
    }
}

@Composable
fun Die(
    value: Int,
    isHeld: Boolean,
    canHold: Boolean,
    onToggleHold: () -> Unit
) {

    val diceImage = when (value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(
                id = diceImage
            ),
            contentDescription = "Dice showing $value",
            modifier = Modifier
                .size(56.dp)
                .clickable(
                    enabled = canHold,
                    onClick = onToggleHold
                )
        )

        if (isHeld) {
            Text("HOLD")
        }
    }
}

private fun YahtzeeCategory.displayName(): String {

    return when (this) {
        YahtzeeCategory.THREE_OF_A_KIND -> "Three of a Kind"
        YahtzeeCategory.FOUR_OF_A_KIND -> "Four of a Kind"
        YahtzeeCategory.FULL_HOUSE -> "Full House"
        YahtzeeCategory.SMALL_STRAIGHT -> "Small Straight"
        YahtzeeCategory.LARGE_STRAIGHT -> "Large Straight"
        YahtzeeCategory.YAHTZEE -> "Yahtzee"
        YahtzeeCategory.CHANCE -> "Chance"
    }
}
