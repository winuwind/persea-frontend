package ru.persea.frontend.ui.screens.details

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.persea.frontend.data.model.products.FactorDto
import ru.persea.frontend.data.model.products.ProductDto

@Composable
fun ProductDetailContent(product: ProductDto) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ProductImage(imageUri = product.imageURI)
        }

        item {
            ProductName(name = product.name)
        }

        item {
            ProductRating(rating = product.rating)
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ingredients & Composition",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(product.factors ?: emptyList()) { factor ->
            FactorCard(factor = factor)
        }
    }
}

@Composable
fun ProductImage(imageUri: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // Реализация загрузки изображения
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Product Image Placeholder")
        }
    }
}

@Composable
fun ProductName(name: String?) {
    Text(
        text = name ?: "Unknown Product",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun ProductRating(rating: Int?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star",
                tint = if (rating != null && index < rating)
                    Color(0xFFFFD700) else Color(0xFFE0E0E0),
                modifier = Modifier.size(24.dp)
            )
            if (index < 4) Spacer(modifier = Modifier.width(4.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${rating ?: 0}/5",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun FactorCard(factor: FactorDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = factor.name ?: "Unknown",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            // Добавьте остальную логику отображения factor
        }
    }
}