package com.example.kotlin_yahtzee

import kotlin.math.max
import kotlin.random.Random

open class Player (playerName: String){
    val playerName: String = playerName
    var scorecard = arrayListOf<String>("", "", "", "", "", "", "", "", "", "", "", "", "")


    fun updateScorecard(category: Int, diceHand: ArrayList<Int>) {
        diceHand.sort()

        val score = calculateScore(category, diceHand)

        scorecard[category] = score.toString()

    }

    private fun calculateScore(category: Int, diceHand: ArrayList<Int>): Int {
        return when (category + 1) {
            1, 2, 3, 4, 5, 6 -> calculateTopScores(category, diceHand)
            7, 8 -> calculateXOfAKindScores(category, diceHand)
            9 -> 25 //full house
            10, 11 -> 40 //straights
            12 -> 50 //yahtzee
            else -> calculateDiceTotalValue(diceHand)

        }
    }

    private fun calculateTopScores(category: Int, diceHand: ArrayList<Int>): Int {
        var score = 0
        diceHand.forEach { die ->
            if (die == category + 1) {
                score += die
            }
        }
        return score
    }

    private fun calculateXOfAKindScores(category: Int, diceHand: ArrayList<Int>): Int {
        val maxCount = diceHand
            .groupingBy { it }
            .eachCount()
            .maxOf { it.value }

        return if (category + 1 == 8 && maxCount >= 4) {
            calculateDiceTotalValue(diceHand)
        } else if (category + 1 == 7 && maxCount >= 3) {
            calculateDiceTotalValue(diceHand)
        } else {
            0
        }
    }

    private fun calculateDiceTotalValue(diceHand: ArrayList<Int>): Int {
        return diceHand.sum()
    }
}

