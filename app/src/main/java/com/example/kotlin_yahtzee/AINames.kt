package com.example.kotlin_yahtzee

object AINames {

    private val names:List<String> = listOf("Glados", "HAL9000", "Skynet", "Ultron", "Shodan", "Mother Brain", "Am")

    fun getName():String {
        return names.random()
    }
}