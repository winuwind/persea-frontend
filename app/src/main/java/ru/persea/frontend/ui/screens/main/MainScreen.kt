package ru.persea.frontend.ui.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.persea.frontend.ui.components.BottomBar
import ru.persea.frontend.ui.screens.details.ProductDetailScreen
import ru.persea.frontend.ui.screens.scan.ScanScreen
import ru.persea.frontend.ui.screens.profile.ProfileScreen
import ru.persea.frontend.ui.screens.search.SearchScreen
import ru.persea.frontend.ui.screens.support.SupportScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavController) { // Принимаем navController из AppNavGraph

    val mainNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(mainNavController) }
    ) { padding ->

        NavHost(
            navController = mainNavController,
            startDestination = "profile",
            modifier = Modifier.padding(padding)
        ) {

            composable("scan") {
                ScanScreen(
                    onProductClick = { productId ->
                        // При клике на продукт переходим к деталям
                        mainNavController.navigate("product_detail/$productId")
                    }
                ) }
            composable("search") {
                SearchScreen(
                    onProductClick = { productId ->
                        // При клике на продукт переходим к деталям
                        mainNavController.navigate("product_detail/$productId")
                    }
                )
            }
            composable("support") { SupportScreen() }
            composable("profile") { ProfileScreen(navController) }

            // Добавляем экран деталей внутри MainScreen
            composable(
                route = "product_detail/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.LongType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getLong("productId")
                ProductDetailScreen(
                    productId = productId,
                    navController = mainNavController
                )
            }
        }
    }
}