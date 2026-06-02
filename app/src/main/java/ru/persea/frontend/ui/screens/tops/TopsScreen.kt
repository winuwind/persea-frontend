package ru.persea.frontend.ui.screens.tops

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.persea.frontend.data.api.products.ProductRetrofitClient
import ru.persea.frontend.data.model.products.ProductDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopsScreen(navController: NavController) {
    var products by remember { mutableStateOf<List<ProductDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val result = ProductRetrofitClient.api.getProducts(
                query = null,
                categoryId = null,
                brandIds = null,
                minRating = 70,
                maxRating = null,
                page = 0,
                size = 40
            )
            products = result.sortedByDescending { it.rating ?: 0 }
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Лучшие оценки") })
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
                products.isEmpty() -> {
                    Text(
                        text = "Пока нет продуктов с оценкой",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Подиум",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                products.take(3).forEachIndexed { index, product ->
                                    TopPodiumItem(
                                        rank = index + 1,
                                        product = product,
                                        onClick = { product.id?.let { navController.navigate("product_detail/$it") } }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Рейтинг",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(products) { product ->
                            ProductRankRow(
                                product = product,
                                onClick = { product.id?.let { navController.navigate("product_detail/$it") } }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopPodiumItem(rank: Int, product: ProductDto, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(60.dp)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$rank",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = when (rank) {
                    1 -> Color(0xFFFFD700)
                    2 -> Color(0xFFC0C0C0)
                    else -> Color(0xFFCD7F32)
                }
            )
        }
        Text(
            text = product.name?.take(15) ?: "",
            fontSize = 12.sp,
            maxLines = 2,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "${product.rating ?: 0}/100",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ProductRankRow(product: ProductDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name ?: "Без названия",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Рейтинг: ${product.rating ?: 0}/100",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = "${product.rating ?: 0}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}