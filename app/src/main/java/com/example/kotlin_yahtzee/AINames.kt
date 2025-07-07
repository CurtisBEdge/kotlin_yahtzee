package com.example.kotlin_yahtzee

class AINames {

    val names:List<String> = listOf("Glados", "HAL9000", "Skynet", "Ultron", "Shodan", "Mother Brain", "Am")

    fun getName():String {
        return names.random()
    }
}