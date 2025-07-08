package com.example.kotlin_yahtzee

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel(): ViewModel() {
    private val gameModel = GameModel()
    private val playerName = "Brian"
    private var currentPlayer = Player()
    var currentScorecard =  arrayListOf<String>("2", "4", "9", "16", "15", "18", "25", "18", "25", "30", "0", "0", "17")
    private var diceHand = arrayListOf(2, 5, 6, 3, 1)
    var isRollingDone by mutableStateOf(false)
    private set

    var isPlayerTurn by mutableStateOf(true)
    private set

    fun createGame(playerName: String, aiPlayers: Int) {
        gameModel.createGame(playerName, aiPlayers)
    }

    fun getPlayerName(): String {
        return playerName
    }

    fun getScoreCard(): ArrayList<String> {
        return currentScorecard
    }

    fun getDiceHand(): ArrayList<Int> {
        return diceHand
    }

    fun getIsPlayerTurn(): Boolean {
        return isPlayerTurn
    }

    fun getIsRollingDone(): Boolean {
        return isRollingDone
    }

    fun reRollDice(diceToKeep: List<Boolean>) {
        isRollingDone = true
    }

    fun keepAllDice() {
        isRollingDone = true
    }

    fun chooseScoreCategory(category: Int) {
        isRollingDone = false
    }
}