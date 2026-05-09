package ru.persea.frontend.ui.screens.history

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HistoryScreen() {

    val history = listOf("Milk", "Bread", "Cheese")

    LazyColumn {
        items(history) {
            Text(it, modifier = Modifier.padding(16.dp))
        }
    }
}