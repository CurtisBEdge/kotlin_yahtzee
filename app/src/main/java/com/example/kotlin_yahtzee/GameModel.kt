package com.example.kotlin_yahtzee

class GameModel {
    private var currentPlayer = 0
    private var currentRound = 1

    private var playerList: ArrayList<Player> = arrayListOf()


    fun createGame(playerName: String, aiPlayers: Int) {
        playerList.clear()
        val newPlayer = Player(playerName)
        // need to add ability to add AI players to the playerList
        playerList.add(newPlayer)
        playerList.add(Player("Brian"))
//        playerList.add(Player("Steve"))
        currentPlayer = 0
        currentRound = 1
    }

    fun getCurrentPlayer(): Player {
        return playerList[currentPlayer]
    }

    fun getCurrentRound(): Int {
        return currentRound
    }

    fun incrementCurrentPlayer() {
        currentPlayer ++
        if (currentPlayer >= playerList.size) {
            currentPlayer = 0
            currentRound ++
        }
    }

    fun updateCurrentPlayerScore(category: Int, diceHand: ArrayList<Int>) {
        playerList[currentPlayer].updateScorecard(category, diceHand)

    }

    fun getFinalScores(): List<String> {
        val playerScoreList = arrayListOf<String>()
        var scores: ArrayList<Int> = arrayListOf()


        playerList.forEach { player ->
            val playerScore = player.calculateFinalScore()
            scores.add(playerScore)
            playerScoreList.add("${player.playerName} scored $playerScore")
        }

        val winningList = findWinningPlayer(scores)

        if (winningList.size == 1) {
            playerScoreList.add("${playerList[winningList[0]].playerName} is the winner")
        } else {
            playerScoreList.add("It's a draw!")
        }

        return playerScoreList
    }

    private fun findWinningPlayer(scores: ArrayList<Int>): List<Int> {
        val highestScore = scores.max()

        return scores.withIndex()
            .filter {it.value == highestScore}
            .map {it.index}
    }
}