package ru.persea.frontend.ui.screens.history

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
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.persea.frontend.data.api.users.UserRetrofitClient
import ru.persea.frontend.data.model.users.ProductDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    var viewedProducts by remember { mutableStateOf<List<ProductDto>>(emptyList()) }
    var scannedProducts by remember { mutableStateOf<List<ProductDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val viewed = UserRetrofitClient.api.getMyViewedProducts(page = 0, size = 50)
            val scanned = UserRetrofitClient.api.getMyScannedProducts(page = 0, size = 50)
            viewedProducts = viewed
            scannedProducts = scanned
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("История") })
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
                    Text(
                        text = "Ошибка: $error",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = "Просмотренные товары",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            if (viewedProducts.isEmpty()) {
                                Text(
                                    text = "Нет просмотренных товаров",
                                    color = Color.Gray,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            } else {
                                viewedProducts.forEach { product ->
                                    HistoryItem(
                                        product = product,
                                        onClick = { product.id?.let { navController.navigate("product_detail/$it") } }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Отсканированные товары",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            if (scannedProducts.isEmpty()) {
                                Text(
                                    text = "Нет отсканированных товаров",
                                    color = Color.Gray,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            } else {
                                scannedProducts.forEach { product ->
                                    HistoryItem(
                                        product = product,
                                        onClick = { product.id?.let { navController.navigate("product_detail/$it") } }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(product: ProductDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = product.name ?: "Без названия",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Рейтинг: ${product.rating ?: 0}/100",
                fontSize = 12.sp,
                color = Color.Gray
            )
            product.createdAt?.let {
                Text(
                    text = "Дата: ${it.toString().take(10)}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun Spacer(modifier: Modifier = Modifier, height: Int) {
    androidx.compose.foundation.layout.Spacer(modifier = modifier.height(height.dp))
}