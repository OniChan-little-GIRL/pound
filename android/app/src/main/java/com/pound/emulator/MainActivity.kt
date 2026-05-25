package com.pound.emulator

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.max

enum class PoundTab { Home, Games, Settings }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val selectedFolder = mutableStateOf<String?>(null)
        val pickFolder = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            if (uri != null) {
                contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                selectedFolder.value = uri.toString()
            }
        }

        setContent {
            MaterialTheme {
                PoundApp(
                    selectedFolder = selectedFolder.value,
                    onSelectFolder = { pickFolder.launch(null) }
                )
            }
        }
    }
}

@Composable
fun PoundApp(selectedFolder: String?, onSelectFolder: () -> Unit) {
    var currentTab by remember { mutableStateOf(PoundTab.Home) }
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Row(modifier = Modifier.padding(padding).fillMaxSize()) {
            NavigationRail {
                PoundTab.entries.forEach { tab ->
                    NavigationRailItem(
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        label = { Text(tab.name) },
                        icon = {}
                    )
                }
            }
            when (currentTab) {
                PoundTab.Home -> HomeScreen(selectedFolder, onSelectFolder)
                PoundTab.Games -> GamesScreen(selectedFolder)
                PoundTab.Settings -> SettingsScreen()
            }
        }
    }
}

@Composable
private fun HomeScreen(selectedFolder: String?, onSelectFolder: () -> Unit) {
    val abi = Build.SUPPORTED_ABIS.firstOrNull().orEmpty()
    val totalMemoryGb = (Runtime.getRuntime().maxMemory() / (1024 * 1024 * 1024)).toInt()
    val estimatedPipelinesCacheMb = max(128, totalMemoryGb * 256)

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("SoC Profile", style = MaterialTheme.typography.titleLarge)
                Text("Model: ${Build.MANUFACTURER} ${Build.MODEL}")
                Text("ABI: $abi")
                Text("Pipeline cache budget: ~${estimatedPipelinesCacheMb}MB")
            }
        }

        Button(onClick = onSelectFolder) { Text("Select Folder") }
        Text("Selected: ${selectedFolder ?: "Not selected"}")

        Button(
            onClick = {
                NativeCore.initCore("{\"abi\":\"$abi\",\"cacheMb\":$estimatedPipelinesCacheMb}")
                selectedFolder?.let { NativeCore.startWorker(it) }
            },
            enabled = selectedFolder != null
        ) {
            Text("Start Game")
        }
    }
}

@Composable
private fun GamesScreen(selectedFolder: String?) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Games")
        Text(selectedFolder ?: "No folder selected")
    }
}

@Composable
private fun SettingsScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Settings")
        Text("Snapdragon profile tuning will be configurable here.")
    }
}
