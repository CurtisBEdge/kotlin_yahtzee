package com.example.kotlin_yahtzee

import androidx.lifecycle.ViewModel

class GameViewModel(): ViewModel() {
    private val gameModel = GameModel()
    private val playerName = "Brian"
    private var currentPlayer = Player()
    private var currentScorecard = arrayListOf<String>("2", "4", "9", "16", "15", "18", "25", "18", "25", "30", "0", "0", "17")
    private var diceHand = arrayListOf(2, 5, 6, 3, 1)
    private var rollingDone = true

    fun createGame(playerName: String, aiPlayers: Int) {
        gameModel.createGame(playerName, aiPlayers)
    }

    fun getPlayerName(): String {
        return playerName
    }

    fun getScorecardCategory(category: Int): String {
        return currentScorecard[category]
    }

    fun getScoreCard(): ArrayList<String> {
        return currentScorecard
    }

    fun getDiceHand(): ArrayList<Int> {
        return diceHand
    }

    fun getRollingDone(): Boolean {
        return rollingDone
    }
}