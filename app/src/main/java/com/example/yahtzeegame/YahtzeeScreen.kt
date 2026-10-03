package com.example.yahtzeegame

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun YahtzeeScreen(
    viewModel: YahtzeeViewModel
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Yahtzee"
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            viewModel.diceValues.forEach { value ->
                Die(
                    value = value
                )
            }
        }

        Button(
            onClick = {
                viewModel.rollWithCoroutine()
            }
        ) {
            Text("Roll")
        }

        viewModel.categoryScores.forEach { result ->

            Text(
                text = "${result.category}: ${result.score}"
            )
        }
    }
}

@Composable
fun Die(value: Int) {

    val diceImage = when (value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1
    }

    Image(
        painter = painterResource(
            id = diceImage
        ),
        contentDescription = "Dice showing $value",
        modifier = Modifier.size(60.dp)
    )
}
