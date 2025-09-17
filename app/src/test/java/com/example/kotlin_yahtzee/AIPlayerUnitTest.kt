package com.example.kotlin_yahtzee

import org.junit.Test

import org.junit.Assert.*

class AIPlayerUnitTest {

    @Test fun chooseDiceWith4OfAKind() {
        val aiPlayer = AIPlayer()

        val diceHand = arrayListOf(6, 6, 6, 6, 5)

        assert(aiPlayer.chooseDiceToKeep(diceHand) == arrayListOf(true, true, true, true, false))
    }

    @Test fun chooseDiceWithFullHouse() {
        val aiPlayer = AIPlayer()

        val diceHand = arrayListOf(2, 2, 3, 3, 3)
        val diceToKeep = aiPlayer.chooseDiceToKeep(diceHand)
        println(diceToKeep)
        assert(aiPlayer.chooseDiceToKeep(diceHand) == arrayListOf(true, true, true, true, true))
    }


}