package com.pound.emulator.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GamesScreen() {
    var selectedDirectory by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri -> selectedDirectory = uri }
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Games Library", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { launcher.launch(null) }) {
            Text(text = if (selectedDirectory == null) "Select Games Folder" else "Change Folder")
        }

        if (selectedDirectory != null) {
            Text(text = "Selected: ${selectedDirectory?.path}", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* Start Game Logic */ },
            enabled = selectedDirectory != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Start Game")
        }
    }
}
