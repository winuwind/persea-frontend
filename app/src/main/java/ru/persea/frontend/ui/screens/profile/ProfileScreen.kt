package ru.persea.frontend.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.persea.frontend.ui.screens.viewModel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    val isLoading = viewModel.isLoading
    val viewedProducts = viewModel.viewedProducts
    val scannedProducts = viewModel.scannedProducts
    val favorites = viewModel.favorites

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Профиль") })
        }
    ) { paddingValues ->
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
                    Text("История поиска")
                }
            }
        }
    }
}