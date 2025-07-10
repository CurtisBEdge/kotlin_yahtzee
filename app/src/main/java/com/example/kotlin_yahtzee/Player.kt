package com.example.kotlin_yahtzee

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
            9 -> calculateFullHouse(diceHand)
            10, 11 -> calculateStraight(category, diceHand)
            12 -> calculateYatzy(diceHand)
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
        val maxCount = getDiceCount(diceHand).maxOf { it.value }

        return if (category + 1 == 8 && maxCount >= 4) {
            calculateDiceTotalValue(diceHand)
        } else if (category + 1 == 7 && maxCount >= 3) {
            calculateDiceTotalValue(diceHand)
        } else {
            0
        }
    }

    private fun calculateFullHouse(diceHand: ArrayList<Int>): Int {
        val diceCount = getDiceCount(diceHand)

        return if (diceCount.maxOf { it.value } == 3 && diceCount.minOf{ it.value } == 2) {
            25
        } else 0
    }

    private fun calculateStraight(category: Int, diceHand: ArrayList<Int>): Int {
        var straightCount = 0

        // For loop for the first 4 dice rather than foreach for all, as it's referencing the next value in the array. Foreach would reference outside the array's bounds
        for (i in 0..3 ) {
            if (diceHand[i] == diceHand[i + 1] -1) { // the hand has been sorted. This checks if the next die -1 is the same as the current
                straightCount ++
            } else if (diceHand[i] < diceHand[i + 1] -1) { // This checks if the next die is more than 1 higher. This handles if there's 2 of the same value
                straightCount = 0
            }
        }

        return if (category + 1 == 11 && straightCount == 4) {
            40
        } else if (category + 1 == 10 && straightCount >= 3) {
            30
        } else 0
    }

    private fun calculateYatzy(diceHand: ArrayList<Int>): Int {
        val maxCount = getDiceCount(diceHand).maxOf { it.value }

        return if (maxCount == 5) {
            50 } else 0
    }

    private fun calculateDiceTotalValue(diceHand: ArrayList<Int>): Int {
        return diceHand.sum()
    }

    private fun getDiceCount(diceHand: ArrayList<Int>): Map<Int, Int> {
        return diceHand
            .groupingBy { it }
            .eachCount()
    }
}

