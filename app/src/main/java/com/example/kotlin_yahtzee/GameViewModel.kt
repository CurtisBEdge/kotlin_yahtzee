package com.example.kotlin_yahtzee

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GameViewModel(): ViewModel() {
    private val gameModel = GameModel()
    private lateinit var currentPlayer: Player

    var diceHand = arrayListOf(rollDie(), rollDie(), rollDie(), rollDie(), rollDie())
    private set

    var diceToKeep = arrayListOf<Boolean>(false, false, false, false, false)

    var isRollingDone by mutableStateOf(false)
    private set

    var isPlayerTurn by mutableStateOf(true)
    private set

    var rerolls by mutableIntStateOf(2)
    private set

    var gameFinished by mutableStateOf(false)
    private set

    fun createGame(playerName: String, aiPlayers: Int) {
        gameFinished = false
        gameModel.createGame(playerName, aiPlayers)
        setCurrentPlayer()
    }

    fun setCurrentPlayer() {
        currentPlayer = gameModel.getCurrentPlayer()
    }

    fun getScoreCard(): ArrayList<String> {
        return currentPlayer.scorecard
    }

    fun getPlayerName(): String {
        return currentPlayer.playerName
    }

    fun getIsPlayerTurn(): Boolean {
        return isPlayerTurn
    }

    fun reRollDice(diceToKeep: List<Boolean>) {
        rerolls -= 1
        if (rerolls <= 0) {
            isRollingDone = true
        }
        diceToKeep.forEachIndexed{index, die: Boolean ->
            if (!die) {
                diceHand[index] = rollDie()
            }
        }

    }

    private fun rollDie(): Int {
        return Random.nextInt(1, 7)
    }

    private fun resetDiceToKeep() {
        diceToKeep = arrayListOf(false, false, false, false, false)
    }

    fun keepAllDice() {
        isRollingDone = true
    }

    fun chooseScoreCategory(category: Int) {
        isRollingDone = false
        gameModel.updateCurrentPlayerScore(category, diceHand)
        resetTurn()
    }

    private fun resetTurn() {
        gameModel.incrementCurrentPlayer()
        if (gameModel.getCurrentRound() > 13) {
            gameFinished = true
        }
        resetDiceToKeep()
        reRollDice(diceToKeep)
        rerolls = 2
        setCurrentPlayer()
    }

    fun getFinalScores(): List<String> {
        return gameModel.getFinalScores()
    }
}