package com.example.kotlin_yahtzee

class AIPlayer : Player(AINames.getName())  {



    fun chooseDiceToKeep(diceHand: ArrayList<Int>): List<Boolean> {
        calculateAllOdds(diceHand)
        chooseDice()


        return listOf(false, false, false, false, false)
    }


    private fun calculateAllOdds(diceHand: ArrayList<Int>) {

    }

    private fun chooseDice() {

    }


    fun chooseCategory(diceHand: ArrayList<Int>) {

    }
}