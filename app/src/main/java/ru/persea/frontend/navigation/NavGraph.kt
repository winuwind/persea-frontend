package ru.persea.frontend.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.persea.frontend.ui.screens.details.ProductDetailScreen
import ru.persea.frontend.ui.screens.history.HistoryScreen
import ru.persea.frontend.ui.screens.main.MainScreen
import ru.persea.frontend.ui.screens.onboarding.OnboardingScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {

        composable("onboarding") {
            OnboardingScreen {
                navController.navigate("main") {
                    popUpTo("onboarding") { inclusive = true }
                }
            }
        }

        composable("main") {
            MainScreen(navController = navController) // Передаем navController
        }

        composable("history") {
            HistoryScreen()
        }

        // Добавляем экран деталей продукта
        composable(
            route = "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId")
            ProductDetailScreen(
                productId = productId,
                navController = navController
            )
        }
    }
}