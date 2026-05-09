package ru.persea.frontend.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.persea.frontend.ui.screens.details.FoodDetailsScreen
import ru.persea.frontend.ui.screens.history.HistoryScreen
import ru.persea.frontend.ui.screens.main.MainScreen
import ru.persea.frontend.ui.screens.onboarding.OnboardingScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {

        composable("onboarding") {
            OnboardingScreen {
                navController.navigate("main")
            }
        }

        composable("main") {
            MainScreen()
        }

        composable("details/{food}") {
            FoodDetailsScreen()
        }

        composable("history") {
            HistoryScreen()
        }
    }
}