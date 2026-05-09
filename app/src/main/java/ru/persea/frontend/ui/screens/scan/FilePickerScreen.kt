package ru.persea.frontend.ui.screens.scan

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun FilePickerScreen(onDone: () -> Unit) {

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        onDone()
    }

    LaunchedEffect(Unit) {
        launcher.launch("image/*")
    }
}