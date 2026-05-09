package ru.persea.frontend.ui.screens.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanBottomSheet(
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {

    ModalBottomSheet(onDismissRequest = onDismiss) {

        Column(modifier = Modifier.padding(16.dp)) {

            Button(onClick = { onSelect("camera") }) {
                Text("Camera")
            }

            Button(onClick = { onSelect("file") }) {
                Text("From file")
            }
        }
    }
}