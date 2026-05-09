package ru.persea.frontend.ui.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.persea.frontend.ui.components.BottomBar
import ru.persea.frontend.ui.screens.scan.ScanScreen
import ru.persea.frontend.ui.screens.profile.ProfileScreen
import ru.persea.frontend.ui.screens.search.SearchScreen
import ru.persea.frontend.ui.screens.support.SupportScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "profile",
            modifier = Modifier.padding(padding)
        ) {

            composable("scan") { ScanScreen() }
            composable("search") { SearchScreen() }
            composable("support") { SupportScreen() }
            composable("profile") { ProfileScreen(navController) }
        }
    }
}