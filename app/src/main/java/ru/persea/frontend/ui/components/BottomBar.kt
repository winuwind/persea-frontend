package ru.persea.frontend.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BottomBar(navController: NavController) {

    data class BottomItem(
        val route: String,
        val icon: ImageVector,
        val label: String
    )

    val items = listOf(
        BottomItem("scan", Icons.Default.QrCodeScanner, "Scan"),
        BottomItem("search", Icons.Default.Search, "Search"),
        BottomItem("support", Icons.Default.Info, "Support"),
        BottomItem("profile", Icons.Default.Person, "Profile")
    )

    NavigationBar {

        items.forEach { item ->

            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}