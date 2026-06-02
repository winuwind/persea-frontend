package ru.persea.frontend.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import ru.persea.frontend.data.api.users.AuthInterceptor

@Composable
fun BottomBar(navController: NavController) {
    val isAdmin = remember { AuthInterceptor.isAdmin() }

    data class BottomItem(
        val route: String,
        val icon: ImageVector,
        val label: String
    )

    val baseItems = listOf(
        BottomItem("scan", Icons.Default.QrCodeScanner, "Scan"),
        BottomItem("search", Icons.Default.Search, "Search"),
        BottomItem("recommendations", Icons.Default.Recommend, "For You"),
        BottomItem("tops", Icons.Default.Star, "Tops"),
        BottomItem("factors", Icons.Default.BarChart, "Factors"),
        BottomItem("profile", Icons.Default.Person, "Profile")
    )

    val adminItems = listOf(
        BottomItem("admin", Icons.Default.AdminPanelSettings, "Admin")
    )

    val items = if (isAdmin) baseItems + adminItems else baseItems

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