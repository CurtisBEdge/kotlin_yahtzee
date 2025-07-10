package com.example.kotlin_yahtzee

class GameModel {
    private var currentPlayer = 0
    private var currentRound = 1

    private var playerList: ArrayList<Player> = arrayListOf()


    fun createGame(playerName: String, aiPlayers: Int) {
        val newPlayer = Player(playerName)
        // need to add ability to add AI players to the playerList
        playerList.add(newPlayer)
//        playerList.add(Player("Brian"))
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

        playerList.forEach { player ->
            val playerScore = player.calculateFinalScore()
            playerScoreList.add("${player.playerName} scored $playerScore")
        }

        return playerScoreList
    }
}