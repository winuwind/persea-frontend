package ru.persea.frontend.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.persea.frontend.data.api.auth.TokenStorage
import ru.persea.frontend.ui.screens.viewModel.AuthViewModel
import ru.persea.frontend.ui.screens.viewModel.AuthViewModelFactory
import ru.persea.frontend.ui.screens.viewModel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val tokenStorage = remember { TokenStorage(context) }
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(context)
    )
    var isLoggedIn by remember { mutableStateOf(tokenStorage.isLoggedIn()) }

    LaunchedEffect(Unit) {
        profileViewModel.loadUserData()
    }

    val isLoading = profileViewModel.isLoading
    val viewedProducts = profileViewModel.viewedProducts
    val scannedProducts = profileViewModel.scannedProducts
    val favorites = profileViewModel.favorites

    fun logout() {
        // Полный выход с очисткой всех данных
        authViewModel.logout()
        isLoggedIn = false

        // Сброс навигации и переход на экран входа
        navController.navigate("login") {
            popUpTo(0) { inclusive = true }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Профиль") })
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Пользователь", style = MaterialTheme.typography.titleLarge)
                            Text("Аккаунт активен", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { logout() },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFEF4444)
                                )
                            ) {
                                Text("Выйти")
                            }
                        }
                    }
                }

                item {
                    Text("Просмотренные товары", style = MaterialTheme.typography.titleMedium)
                }

                if (viewedProducts.isNotEmpty()) {
                    items(viewedProducts.take(5)) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { product.id?.let { id -> navController.navigate("product_detail/$id") } }
                        ) {
                            Text(
                                text = product.name ?: "Без названия",
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                } else {
                    item {
                        Text("Нет просмотренных товаров", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Отсканированные товары", style = MaterialTheme.typography.titleMedium)
                }

                if (scannedProducts.isNotEmpty()) {
                    items(scannedProducts.take(5)) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { product.id?.let { id -> navController.navigate("product_detail/$id") } }
                        ) {
                            Text(
                                text = product.name ?: "Без названия",
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                } else {
                    item {
                        Text("Нет отсканированных товаров", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Избранное", style = MaterialTheme.typography.titleMedium)
                }

                if (favorites.isNotEmpty()) {
                    items(favorites) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { product.id?.let { id -> navController.navigate("product_detail/$id") } }
                        ) {
                            Text(
                                text = product.name ?: "Без названия",
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                } else {
                    item {
                        Text("Нет избранных товаров", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { navController.navigate("history") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("История")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { navController.navigate("recommendations") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Рекомендации")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { navController.navigate("factors") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Факторы качества")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { navController.navigate("tops") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Лучшие оценки")
                    }
                }
            }
        }
    }
}