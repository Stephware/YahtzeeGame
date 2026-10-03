package com.example.yahtzeegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yahtzeegame.ui.theme.YahtzeeGameTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            YahtzeeGameTheme {

                val viewModel: YahtzeeViewModel = viewModel()

                YahtzeeScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
