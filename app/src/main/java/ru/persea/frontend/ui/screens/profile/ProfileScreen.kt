package ru.persea.frontend.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController) {

    Column(modifier = Modifier.padding(16.dp)) {

        Button(onClick = {}) {
            Text("Edit profile")
        }

        Button(onClick = {
            navController.navigate("history")
        }) {
            Text("Search history")
        }

        Button(onClick = {}) {
            Text("Scan history")
        }

        Button(onClick = {}) {
            Text("Recommendations")
        }
    }
}