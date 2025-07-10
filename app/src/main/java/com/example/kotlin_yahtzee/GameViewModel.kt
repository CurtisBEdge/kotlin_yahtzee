package com.example.kotlin_yahtzee

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GameViewModel(): ViewModel() {
    private val gameModel = GameModel()
    private lateinit var currentPlayer: Player

    var diceHand = mutableStateListOf(rollDie(), rollDie(), rollDie(), rollDie(), rollDie())
    private set

    //var diceToKeep = arrayListOf(false, false, false, false, false)

    var isRollingDone by mutableStateOf(false)
    private set

    var isPlayerTurn by mutableStateOf(true)
    private set

    var rerolls by mutableIntStateOf(2)
    private set

    var gameFinished by mutableStateOf(false)
    private set


    // game run through:
    //
    // createGame - changes state of the gameFinished, rerolls, isRollingDone, and runs create game in the model
    // and sets current player from the gameModel


    // Feed data to the UI

    fun getScoreCard(): ArrayList<String> {
        return currentPlayer.scorecard
    }

    fun getPlayerName(): String {
        return currentPlayer.playerName
    }

    fun getIsPlayerTurn(): Boolean {
        return isPlayerTurn
    }


    // Game running functions

    fun createGame(playerName: String, aiPlayers: Int) {
        gameFinished = false
        rerolls = 2
        isRollingDone = false
        rollDice()
        gameModel.createGame(playerName, aiPlayers)
        setCurrentPlayer()
    }

    fun setCurrentPlayer() {
        currentPlayer = gameModel.getCurrentPlayer()
    }

    fun chooseScoreCategory(category: Int) {
        val workingDiceHand = arrayListOf(diceHand[0], diceHand[1], diceHand[2], diceHand[3], diceHand[4] )
        gameModel.updateCurrentPlayerScore(category, workingDiceHand)
        resetTurn()
    }

    private fun resetTurn() {
        isRollingDone = false
//        resetDiceToKeep()
        //reRollDice(diceToKeep)
        rerolls = 2
        rollDice()
        gameModel.incrementCurrentPlayer()
        if (gameModel.getCurrentRound() > 13) {
            gameFinished = true
            isRollingDone = true
        }
        setCurrentPlayer()
    }


    fun getFinalScores(): List<String> {
        return gameModel.getFinalScores()
    }




    // Dice rolling functions


    private fun rollDice() {
        for (i in 0..4) {
            diceHand[i] = rollDie()
        }
    }

    fun reRollDice(diceSelected: List<Boolean>) {
//        diceSelected.forEachIndexed{ index, die ->
//            diceToKeep[index] = die
//        }
        rerolls -= 1
        if (rerolls <= 0) {
            isRollingDone = true
        }
        diceSelected.forEachIndexed{index, dieToKeep: Boolean ->
            if (!dieToKeep) {
                diceHand[index] = rollDie()
            }
        }
    }

    private fun rollDie(): Int {
        return Random.nextInt(1, 7)
    }





//    private fun resetDiceToKeep() {
//        diceToKeep = arrayListOf(false, false, false, false, false)
//    }

    fun keepAllDice() {
        isRollingDone = true
    }
}