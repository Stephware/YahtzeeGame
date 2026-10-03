package com.example.yahtzeegame

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Yahtzee",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Roll up to 3 times and hold dice between rolls.",
            style = MaterialTheme.typography.bodyMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterHorizontally
            )
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rolls",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${viewModel.rollCount}/3",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Button(
                onClick = {
                    viewModel.rollWithCoroutine()
                },
                modifier = Modifier.fillMaxWidth(),
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
                    text = "Tap a die to hold or release it before the next roll.",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (viewModel.categoryScores.isNotEmpty()) {

                HorizontalDivider()

                Text(
                    text = "Current Combinations",
                    style = MaterialTheme.typography.titleMedium
                )

                viewModel.categoryScores.forEach { result ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = result.category.displayName()
                        )

                        Text(
                            text = result.score.toString()
                        )
                    }
                }
            }

            if (
                viewModel.rollCount == 3 &&
                !viewModel.isRolling
            ) {

                HorizontalDivider()

                Text(
                    text = "Choose a Category",
                    style = MaterialTheme.typography.titleMedium
                )

                viewModel.unusedCategories.forEach { category ->

                    val score = DiceRules.scoreFor(
                        category = category,
                        dice = viewModel.diceValues
                    )

                    OutlinedButton(
                        onClick = {
                            viewModel.selectCategory(category)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${category.displayName()}: $score"
                        )
                    }
                }
            }
        }

        if (viewModel.savedScores.isNotEmpty()) {

            HorizontalDivider()

            Text(
                text = "Scorecard",
                style = MaterialTheme.typography.titleMedium
            )

            viewModel.savedScores.forEach { result ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = result.category.displayName()
                    )

                    Text(
                        text = result.score.toString()
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Score",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = viewModel.totalScore.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        if (viewModel.gameOver) {

            HorizontalDivider()

            Text(
                text = "Game Over",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Final Score: ${viewModel.totalScore}",
                style = MaterialTheme.typography.titleLarge
            )

            Button(
                onClick = {
                    viewModel.resetGame()
                },
                modifier = Modifier.fillMaxWidth()
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Image(
            painter = painterResource(
                id = diceImage
            ),
            contentDescription = "Dice showing $value",
            modifier = Modifier
                .size(52.dp)
                .clickable(
                    enabled = canHold,
                    onClick = onToggleHold
                )
        )

        Text(
            text = if (isHeld) "HELD" else "",
            style = MaterialTheme.typography.labelSmall
        )
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
