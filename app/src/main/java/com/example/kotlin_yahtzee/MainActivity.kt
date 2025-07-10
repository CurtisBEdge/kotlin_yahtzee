package com.example.kotlin_yahtzee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlin_yahtzee.ui.theme.Kotlin_yahtzeeTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kotlin_yahtzeeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    val gameViewModel: GameViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home", modifier = modifier.fillMaxSize()) {

        composable(route = "home") {
            MainScreen(gameViewModel, onNextScreen = {
                navController.navigate("game")
            })
        }

        composable(route = "game") {
            Game(gameViewModel, onNextScreen = {
                navController.navigate("results")
            })
        }

        composable(route = "results") {
            Results(gameViewModel, onNextScreen = {
                navController.navigate("home")
            })
        }

    }
}

@Composable
fun MainScreen (gameViewModel: GameViewModel, onNextScreen: () -> Unit) {

    var nameInput by remember {mutableStateOf("")}
    var aiNumber by remember { mutableStateOf("0") }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(stringResource(R.string.welcome))

        NameEntry(nameInput, onValueChange = {nameInput = it})

        AiNumberEntry(aiNumber, onValueChange = {aiNumber = it})

        Button(onClick = {
            if (aiNumber.isNotEmpty() && aiNumber.isDigitsOnly() && aiNumber.toInt() > -1 && aiNumber.toInt() < 5) {
                gameViewModel.createGame(nameInput, 0)
                onNextScreen()
            } else {
                aiNumber = "0"
                println("Something has gone wrong")
            }
        }) {
            Text("Play Game")
        }
    }
}

@Composable
fun NameEntry(nameInput: String, onValueChange: (String) -> Unit) {

    TextField(
        value = nameInput,
        label = {Text(stringResource(R.string.name_entry))},
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
}

@Composable
fun AiNumberEntry(aiNumber: String, onValueChange: (String) -> Unit) {

    Row {
        Text(stringResource(R.string.number_AI),
            modifier = Modifier.weight(1f)
            )

        TextField(
            value = aiNumber,
            label = {Text(stringResource(R.string.ai_number_entry))},
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
    }
}

// game page content
//
//

@Composable
fun Game(gameViewModel: GameViewModel, onNextScreen: () -> Unit) {

    var selectedDice by remember { mutableStateOf(List(5){false}) }
    var selectedCategory by remember { mutableIntStateOf(-1) }

    Column(modifier = Modifier.fillMaxSize()) {

        Text ( "Player: ${gameViewModel.getPlayerName()}", fontSize = 40.sp)

        ScoreCard(gameViewModel, selectedCategory, onClick = {index: Int ->
            val scorecard = gameViewModel.getScoreCard()
            if (scorecard[index].isEmpty()) {
                selectedCategory = index
            }

        } , modifier = Modifier)

        DiceDisplay(gameViewModel, selectedDice, onToggle = { index ->
            selectedDice = selectedDice.toMutableList().also {
                it[index] = !it[index]
        }})

        if (gameViewModel.gameFinished) {
            Button(onClick = onNextScreen) {
                Text("Game Over, lets see the scores!")
            }
        } else if (!gameViewModel.isRollingDone) {

            Text ("Rolls left: ${gameViewModel.rerolls}", fontSize = 20.sp)
            Row {
                Button(onClick = {
                    gameViewModel.reRollDice(selectedDice)
                    selectedDice = List(5){false}
                }, Modifier.weight(1f).padding(5.dp)) {
                    Text("Reroll dice", fontSize = 25.sp)
                }

                Button(onClick = {
                    gameViewModel.keepAllDice()
                    selectedDice = List(5){false}
                }, Modifier.weight(1f).padding(5.dp)) {
                    Text("Keep all", fontSize = 25.sp)
                }
            }

        } else {
            Button(
                onClick = {
                    if (selectedCategory > -1 && selectedCategory < 13) {
                        gameViewModel.chooseScoreCategory(selectedCategory)
                        selectedCategory = -1
                    }
                }){
                Text("Confirm Category")
            }
        }
    }
}

@Composable
fun ScoreCard(gameViewModel: GameViewModel, selectedCategory: Int, onClick: (Int) -> Unit, modifier: Modifier) {
    val scoreCategories = listOf("Ones", "Twos", "Threes", "Fours", "Fives", "Sixes", "3 of a Kind", "4 of a Kind", "Full House", "Low Straight", "High Straight", "Jacktzee", "Chance")

    val scoreCard = gameViewModel.getScoreCard()

    Column (modifier = Modifier.padding(5.dp, 15.dp)){
        Text("Scorecard:",
            fontSize = 30.sp)

        Row{
            Column(modifier.weight(1f)) {
                scoreCard.forEachIndexed { index, category ->

                    val isSelected = index == selectedCategory
                    if (index <= 5) {
                        Text(text = "${scoreCategories[index]}: $category",
                            fontSize = 25.sp,
                            modifier = if (gameViewModel.isRollingDone && gameViewModel.isPlayerTurn) {
                                Modifier
                                    .clickable { onClick(index) }
                                    .border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = if (isSelected) Color.Red else Color.Transparent
                                    )
                            } else {
                                Modifier
                            }
                        )
                    }
                }
            }

            Column(modifier.weight(1f)) {
                scoreCard.forEachIndexed { index, category ->
                    val isSelected = index == selectedCategory
                    if (index > 5) {
                        Text(text = "${scoreCategories[index]}: $category",
                            fontSize = 25.sp,
                            modifier = if (gameViewModel.isRollingDone && gameViewModel.isPlayerTurn) {
                                Modifier
                                    .clickable { onClick(index) }
                                    .border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = if (isSelected) Color.Red else Color.Transparent
                                    )
                            } else {
                                Modifier
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DiceDisplay(gameViewModel: GameViewModel, selectedDice: List<Boolean>, onToggle: (Int) -> Unit) {

    val diceImages = gameViewModel.diceHand.map { calculateDiceImages(it) }

    Column{
        Text (text = "Click on the dice you wish to keep:",
            fontSize = 20.sp)
    }

    Row {
        diceImages.forEachIndexed { index, painter ->
            val isSelected = selectedDice[index]
            Image(
                painter = painter,
                contentDescription = null,
                modifier = if (gameViewModel.isPlayerTurn && !gameViewModel.isRollingDone) {
                    Modifier
                        .weight(1f)
                        .padding(5.dp)
                        .clickable {
                            onToggle(index)
                        }
                        .border(
                            width = if (isSelected) 3.dp else 0.dp,
                            color = if (isSelected) Color.Red else Color.Transparent
                        )
                } else {
                    Modifier
                        .weight(1f)
                        .padding((5.dp))
                }
            )
        }
    }
}

@Composable
fun calculateDiceImages(dice: Int): Painter {
    return when (dice) {
        1 -> painterResource(R.drawable.dice_one)
        2 -> painterResource(R.drawable.dice_two)
        3 -> painterResource(R.drawable.dice_three)
        4 -> painterResource(R.drawable.dice_four)
        5 -> painterResource(R.drawable.dice_five)
        6 -> painterResource(R.drawable.dice_six)
        else -> error("Invalid dice value $dice")
    }
}


// Results page content
//
//

@Composable
fun Results(gameViewModel: GameViewModel, onNextScreen: () -> Unit) {

    val playerScores = gameViewModel.getFinalScores()

    Column (modifier = Modifier.fillMaxSize()){
        playerScores.forEach{ score ->
            Text(score)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Button(onClick = onNextScreen) {
                Text("Play again?")
            }
        }

    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Kotlin_yahtzeeTheme {
        App()
    }
}

