package ru.persea.frontend.ui.screens.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.persea.frontend.ui.screens.viewModel.ProductDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Long?,
    navController: NavController,
    viewModel: ProductDetailViewModel = viewModel()
) {
    // Загружаем продукт по ID
    productId?.let { id ->
        viewModel.loadProduct(id)
    }

    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = { navController.navigateUp() }) {
                        androidx.compose.material.icons.Icons.Default.ArrowBack.let {
                            androidx.compose.material3.Icon(it, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }
                error != null -> {
                    Text("Error: $error")
                }
                product != null -> {
                    ProductDetailContent(product = product!!)
                }
                else -> {
                    Text("Product not found")
                }
            }
        }
    }
}
