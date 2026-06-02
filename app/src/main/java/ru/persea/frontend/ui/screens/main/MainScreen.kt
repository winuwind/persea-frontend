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
import ru.persea.frontend.ui.screens.admin.AdminPanelScreen
import ru.persea.frontend.ui.screens.details.ProductDetailScreen
import ru.persea.frontend.ui.screens.factors.FactorsScreen
import ru.persea.frontend.ui.screens.profile.ProfileScreen
import ru.persea.frontend.ui.screens.recommendation.RecommendationScreen
import ru.persea.frontend.ui.screens.scan.ScanScreen
import ru.persea.frontend.ui.screens.search.SearchScreen
import ru.persea.frontend.ui.screens.studio.StudioScreen
import ru.persea.frontend.ui.screens.support.SupportScreen
import ru.persea.frontend.ui.screens.tops.TopsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavController) {
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
                        mainNavController.navigate("product_detail/$productId")
                    }
                )
            }
            composable("search") {
                SearchScreen(
                    onProductClick = { productId ->
                        mainNavController.navigate("product_detail/$productId")
                    }
                )
            }
            composable("support") { SupportScreen() }
            composable("profile") { ProfileScreen(navController) }
            composable("recommendations") { RecommendationScreen(navController) }
            composable("factors") { FactorsScreen() }
            composable("tops") { TopsScreen(navController) }
            composable("studio") { StudioScreen(navController = navController) }
            composable("admin") { AdminPanelScreen() }

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