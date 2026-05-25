package com.pound.emulator.ui.screens

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File
import java.util.Scanner

@Composable
fun HomeScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "System Information", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        val cpuInfo = getCpuInfo()
        val socProfile = "Snapdragon Detection: ${if (cpuInfo.contains("Snapdragon")) "Active" else "Generic ARM64"}"

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Architecture: ${Build.SUPPORTED_ABIS.joinToString()}")
                Text(text = "Model: ${Build.MODEL}")
                Text(text = "SoC Profile: $socProfile")
                Text(text = "L3 Cache (Estimated): ${estimateL3Cache()} MB")
            }
        }
    }
}

fun getCpuInfo(): String {
    return try {
        val scanner = Scanner(File("/proc/cpuinfo"))
        val builder = StringBuilder()
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine())
        }
        builder.toString()
    } catch (e: Exception) {
        "Unknown"
    }
}

fun estimateL3Cache(): Int {
    // Dynamic calculation based on SoC - Placeholder for Snapdragon 7s Gen 3 optimizations
    return if (Build.HARDWARE.contains("qcom")) 8 else 4
}
