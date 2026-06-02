package ru.persea.frontend.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.persea.frontend.data.api.auth.TokenStorage
import ru.persea.frontend.data.api.auth.TokenManager
import ru.persea.frontend.ui.screens.auth.AuthWebViewScreen
import ru.persea.frontend.ui.screens.auth.LoginScreen
import ru.persea.frontend.ui.screens.details.ProductDetailScreen
import ru.persea.frontend.ui.screens.factors.FactorsScreen
import ru.persea.frontend.ui.screens.history.HistoryScreen
import ru.persea.frontend.ui.screens.main.MainScreen
import ru.persea.frontend.ui.screens.onboarding.OnboardingScreen
import ru.persea.frontend.ui.screens.recommendation.RecommendationScreen
import ru.persea.frontend.ui.screens.studio.StudioScreen
import ru.persea.frontend.ui.screens.tops.TopsScreen
import ru.persea.frontend.ui.screens.viewModel.AuthViewModel
import ru.persea.frontend.ui.screens.viewModel.AuthViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val tokenStorage = remember { TokenStorage(context) }
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(context)
    )

    var isLoggedIn = tokenStorage.isLoggedIn()

    LaunchedEffect(Unit) {
        TokenManager.onTokenChanged = { newToken ->
            isLoggedIn = newToken != null
            if (newToken == null) {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "onboarding" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToAuthWebView = {
                    navController.navigate("auth_webview")
                },
                viewModel = authViewModel
            )
        }

        composable("auth_webview") {
            AuthWebViewScreen(
                navController = navController,
                onAuthSuccess = { code, codeVerifier ->
                    authViewModel.exchangeCodeForToken(code, codeVerifier) {
                        try {
                            navController.navigate("onboarding") {
                                popUpTo("login") { inclusive = true }
                                popUpTo("auth_webview") { inclusive = true }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            )
        }

        composable("onboarding") {
            OnboardingScreen {
                try {
                    navController.navigate("main") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        composable("main") {
            MainScreen(navController = navController)
        }

        composable("history") {
            HistoryScreen(navController = navController)
        }

        composable("recommendations") {
            RecommendationScreen(navController = navController)
        }

        composable("factors") {
            FactorsScreen()
        }

        composable("tops") {
            TopsScreen(navController)
        }

        composable("studio") {
            StudioScreen()
        }

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