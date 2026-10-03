package com.example.yahtzeegame

import org.junit.Assert.assertEquals
import org.junit.Test

class DiceRulesTest {

    private fun scoresFor(
        dice: List<Int>
    ): List<CategoryScore> {

        return DiceRules
            .getAvailableCategories(dice)
            .map { category ->

                CategoryScore(
                    category = category,
                    score = DiceRules.scoreFor(
                        category = category,
                        dice = dice
                    )
                )
            }
    }

    @Test
    fun yahtzeeHand() {

        val dice = listOf(6, 6, 6, 6, 6)

        val expected = listOf(
            CategoryScore(YahtzeeCategory.THREE_OF_A_KIND, 30),
            CategoryScore(YahtzeeCategory.FOUR_OF_A_KIND, 30),
            CategoryScore(YahtzeeCategory.YAHTZEE, 50),
            CategoryScore(YahtzeeCategory.CHANCE, 30)
        )

        assertEquals(expected, scoresFor(dice))
    }

    @Test
    fun fourOfAKindHand() {

        val dice = listOf(6, 6, 6, 6, 2)

        val expected = listOf(
            CategoryScore(YahtzeeCategory.THREE_OF_A_KIND, 26),
            CategoryScore(YahtzeeCategory.FOUR_OF_A_KIND, 26),
            CategoryScore(YahtzeeCategory.CHANCE, 26)
        )

        assertEquals(expected, scoresFor(dice))
    }

    @Test
    fun threeOfAKindHand() {

        val dice = listOf(2, 2, 2, 4, 6)

        val expected = listOf(
            CategoryScore(YahtzeeCategory.THREE_OF_A_KIND, 16),
            CategoryScore(YahtzeeCategory.CHANCE, 16)
        )

        assertEquals(expected, scoresFor(dice))
    }

    @Test
    fun fullHouseHand() {

        val dice = listOf(3, 3, 3, 5, 5)

        val expected = listOf(
            CategoryScore(YahtzeeCategory.THREE_OF_A_KIND, 19),
            CategoryScore(YahtzeeCategory.FULL_HOUSE, 25),
            CategoryScore(YahtzeeCategory.CHANCE, 19)
        )

        assertEquals(expected, scoresFor(dice))
    }

    @Test
    fun firstLargeStraightHand() {

        val dice = listOf(1, 2, 3, 4, 5)

        val expected = listOf(
            CategoryScore(YahtzeeCategory.SMALL_STRAIGHT, 30),
            CategoryScore(YahtzeeCategory.LARGE_STRAIGHT, 40),
            CategoryScore(YahtzeeCategory.CHANCE, 15)
        )

        assertEquals(expected, scoresFor(dice))
    }

    @Test
    fun secondLargeStraightHand() {

        val dice = listOf(2, 3, 4, 5, 6)

        val expected = listOf(
            CategoryScore(YahtzeeCategory.SMALL_STRAIGHT, 30),
            CategoryScore(YahtzeeCategory.LARGE_STRAIGHT, 40),
            CategoryScore(YahtzeeCategory.CHANCE, 20)
        )

        assertEquals(expected, scoresFor(dice))
    }

    @Test
    fun smallStraightHand() {

        val dice = listOf(1, 1, 2, 3, 4)

        val expected = listOf(
            CategoryScore(YahtzeeCategory.SMALL_STRAIGHT, 30),
            CategoryScore(YahtzeeCategory.CHANCE, 11)
        )

        assertEquals(expected, scoresFor(dice))
    }

    @Test
    fun chanceOnlyHand() {

        val dice = listOf(1, 1, 2, 2, 5)

        val expected = listOf(
            CategoryScore(YahtzeeCategory.CHANCE, 11)
        )

        assertEquals(expected, scoresFor(dice))
    }
}
