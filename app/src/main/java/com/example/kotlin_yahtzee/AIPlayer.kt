package com.example.kotlin_yahtzee

class AIPlayer : Player(AINames.getName())  {



    fun chooseDiceToKeep(diceHand: ArrayList<Int>): List<Boolean> {
        val categoryOddsList = calculateAllOdds(diceHand)
        val chosenCategory = calculateOddPointRatio(diceHand, categoryOddsList)
        val chosenDice = chooseDice(diceHand, chosenCategory)
        return chosenDice
    }


    private fun calculateAllOdds(diceHand: ArrayList<Int>): ArrayList<Double> {
        val oddsList = arrayListOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        for (i in 0..12) {
            when (i) {
                0, 1, 2, 3, 4, 5 -> oddsList[i] = calculateTopSectionOdds(diceHand, i)
                6, 7 -> oddsList[i] = calculateXOfAKindOdds(diceHand, i)
                8 -> oddsList[i] = calculateFullHouseOdds(diceHand)
                9, 10 -> oddsList[i] = calculateStraightOdds(diceHand, i)
                11 -> oddsList[i] = calculateYatzyOdds(diceHand)
            }
        }

        return oddsList
    }

    private fun calculateOddPointRatio(diceHand: ArrayList<Int>, oddsList: ArrayList<Double>): Int {
        val scores = arrayListOf(3, 6, 9, 12, 15, 18, 25, 25, 25, 30, 40, 50, calculateDiceTotalValue(diceHand))
        val multiplier = 1.5
        var highestPointsToOddsRatio = 0.0
        var highestPTORCategory = 0

        scores.forEachIndexed{ index, score ->
            val pTORatio = (score * multiplier) * oddsList[index]

            if (pTORatio > highestPointsToOddsRatio) {
                highestPointsToOddsRatio = pTORatio
                highestPTORCategory = index
            }
        }

        return highestPTORCategory
    }

    private fun calculateTopSectionOdds(diceHand: ArrayList<Int>, category: Int): Double {
        val diceHandCount = getDiceCount(diceHand)
        val diceCount = diceHandCount[category] ?: 0

        return when (diceCount) {
            5, 4, 3 -> 1.0
            2 -> 0.421
            1 -> 0.132
            else -> 0.0
        }
    }

    private fun calculateXOfAKindOdds(diceHand: ArrayList<Int>, category: Int): Double {
        val diceCount = getDiceCount(diceHand).maxOf { it.value }

        if (diceCount >= 4) { return 1.0}

        if (category == 6) {
            return when (diceCount) {
                3 -> 1.0
                2 -> 0.421
                1 -> 0.132
                else -> 0.0
            }
        }

        if(category == 7) {
            return when (diceCount) {
                3 -> 0.306
                2 -> 0.069
                1 -> 0.015
                else -> 0.0
            }
        }

        else {
            return 0.0
        }

    }

    private fun calculateFullHouseOdds(diceHand: ArrayList<Int>): Double {
        val diceCounts = getDiceCount(diceHand)

        if (diceCounts.maxOf{it.value} == 5) {return 1.0} //If diceHand is 5 of a kind, full house is certain
        else if (diceCounts.maxOf { it.value } == 4) {return 0.167} // diceHand == 4 of a kind, 1/6 chance of 5 of a kind

        val diceCountBySize = diceCounts.values.sortedDescending() //Takes the values out of the map pairs of diceCounts and orders them descending

        return if (diceCountBySize[0] == 3 && diceCountBySize[1] == 2) 1.0
        else if (diceCountBySize[0] == 3 && diceCountBySize[1] == 1) 0.333
        else if (diceCountBySize[0] == 2 && diceCountBySize[1] == 2) 0.333
        else if (diceCountBySize[0] == 2 && diceCountBySize[1] == 1) 0.056
        else 0.039
    }

    private fun calculateStraightOdds(diceHand: ArrayList<Int>, category: Int): Double {
        var lowStraightOne = arrayListOf(1, 2, 3, 4)
        var lowStraightTwo = arrayListOf(2, 3, 4, 5)
        var lowStraightThree = arrayListOf(3, 4, 5, 6)

        diceHand.forEach{ die ->

        }
        return 0.01
    }

    private fun calculateYatzyOdds(diceHand: ArrayList<Int>): Double {
        val diceCountMax = getDiceCount(diceHand).maxOf{ it.value }

        return when (diceCountMax) {
            5 -> 1.0
            4 -> 0.167
            3 -> 0.028
            2 -> 0.005
            1 -> 0.001
            else -> 0.0
        }
    }

    private fun chooseDice(diceHand: ArrayList<Int>, chosenCategory: Int): List<Boolean> {

        return listOf(false, false, false, false, false)
    }


    fun choosePointsCategory(diceHand: ArrayList<Int>):Int {
        val potentialScores = arrayListOf(-1, -1, -1, -1, -1, -1, -1 ,-1, -1, -1, -1, -1, -1)
        val diceCounts = getDiceCount(diceHand)
        var highestScore = -1
        var highestScoreCategory = -1

        for (i in 0..12) {
            if(scorecard[i].isEmpty()) potentialScores[i] = calculateScore(i, diceHand)
        }

        if (potentialScores[11] > 0) return 12
        else if (potentialScores[10] > 0) return 11
        else if (potentialScores[9] > 0) return 10
        else if (potentialScores[8] > 0){
            for (i in 3..5){
                if (diceCounts[i] == 3 && potentialScores[i] != -1 ) return 9
            }
        } else {
            for (i in 1..6) {
                if(diceCounts[i]!! >= 3 && potentialScores[i] > 0) return i
                if(diceCounts[i]!! >= 4 && potentialScores[7] > 0) return 8
                if(diceCounts[i]!! >= 3 && potentialScores[6] > 0) return 7
            }
        }

        if(potentialScores[12] > -1) return 13
        if(potentialScores[7] > -1) return 8
        if(potentialScores[11] > -1) return 12
        if(potentialScores[0] > -1) return 1
        if(potentialScores[10] > -1) return 11

        for (i in 0..12) {
            if(potentialScores[i] > highestScore) {
                highestScoreCategory = i + 1
                highestScore = potentialScores[i]
            }
        }

        return highestScoreCategory
    }
}