package ru.persea.frontend.ui.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.persea.frontend.data.api.users.AuthInterceptor
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
import ru.persea.frontend.data.api.auth.TokenManager

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavController) {
    val mainNavController = rememberNavController()
    var isAdmin by remember { mutableStateOf(AuthInterceptor.isAdmin()) }

    // Обновляем роль при изменении токена
    TokenManager.onTokenChanged = { newToken ->
        isAdmin = AuthInterceptor.isAdmin()
    }

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

            // Только для admin
            if (isAdmin) {
                composable("admin") { AdminPanelScreen() }
            }
        }
    }
}