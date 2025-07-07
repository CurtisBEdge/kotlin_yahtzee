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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kotlin_yahtzeeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(
                        modifier = Modifier.padding(innerPadding).fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home", modifier = modifier.fillMaxSize()) {

        composable(route = "home") {
            MainScreen(onNextScreen = {
                navController.navigate("game")
            })
        }

        composable(route = "game") {
            Game(onNextScreen = {
                navController.navigate("results")
            })
        }

        composable(route = "results") {
            Results(onNextScreen = {
                navController.navigate("home")
            })
        }

    }
}

@Composable
fun MainScreen (onNextScreen: () -> Unit) {

    var nameInput by remember {mutableStateOf("")}
    var AiNumber by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(stringResource(R.string.welcome))

        NameEntry(nameInput, onValueChange = {nameInput = it})

        AiNumberEntry(AiNumber, onValueChange = {AiNumber = it})

        Button(onClick = onNextScreen) {
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
fun AiNumberEntry(AiNumber: Int, onValueChange: (Int) -> Unit) {
    
    Row {
        Text(stringResource(R.string.number_AI))

        TextField(
            value = AiNumber,
            label = {Text(stringResource(R.string.ai_number_entry))},
            onValueChange = onValueChange
        )
    }

}

@Composable
fun Game(onNextScreen: () -> Unit) {

    Box(modifier = Modifier.fillMaxSize()) {
        Button(onClick = onNextScreen) {
            Text("Game over, get results")
        }
    }

}

@Composable
fun Results(onNextScreen: () -> Unit) {

    Box(modifier = Modifier.fillMaxSize()) {
        Button(onClick = onNextScreen) {
            Text("You win, back to main menu")
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

