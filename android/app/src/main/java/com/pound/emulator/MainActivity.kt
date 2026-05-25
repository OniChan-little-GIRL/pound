package com.pound.emulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pound.emulator.ui.theme.PoundTheme
import com.pound.emulator.ui.screens.HomeScreen
import com.pound.emulator.ui.screens.GamesScreen
import com.pound.emulator.ui.screens.SettingsScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PoundTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Games", "Settings")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.PlayArrow, Icons.Filled.Settings)

    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail {
            items.forEachIndexed { index, item ->
                NavigationRailItem(
                    icon = { Icon(icons[index], contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        navController.navigate(item.lowercase())
                    }
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(navController, startDestination = "home") {
                composable("home") { HomeScreen() }
                composable("games") { GamesScreen() }
                composable("settings") { SettingsScreen() }
            }
        }
    }
}
