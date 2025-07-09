package com.example.kotlin_yahtzee

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
            7, 8 -> 45 //Xofakind
            9 -> 25 //full house
            10, 11 -> 40 //straights
            12 -> 50 //yahtzee
            else -> 3

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
}

