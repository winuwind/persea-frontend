package ru.persea.frontend.ui.screens.recommendation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.persea.frontend.ui.screens.viewModel.RecommendationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationScreen(
    navController: NavController,
    viewModel: RecommendationViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadRecommendations()
    }

    val products = viewModel.products
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Рекомендации") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Ошибка: $error")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadRecommendations() }) {
                            Text("Повторить")
                        }
                    }
                }
                products.isEmpty() -> {
                    Text(
                        text = "Нет рекомендаций",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(products) { product ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        product.id?.let { productId ->
                                            navController.navigate("product_detail/$productId")
                                        }
                                    },
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = product.name ?: "Без названия",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Рейтинг: ${product.rating ?: 0}/100",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}