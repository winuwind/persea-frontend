package ru.persea.frontend.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.persea.frontend.data.model.products.*

@Composable
fun ProductDetailContent(
    product: ProductResponse,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                ProductImage(imageUri = product.imageURI)
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }
        }

        item {
            ProductName(name = product.name)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProductRating(rating = product.rating)
                if (product.brand != null) {
                    Text(
                        text = product.brand?.name ?: "",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        if (product.category != null) {
            item {
                InfoRow(label = "Категория", value = product.category?.name ?: "")
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Числовые показатели",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (!product.numericFactors.isNullOrEmpty()) {
            items(product.numericFactors) { factor ->
                NumericFactorCard(factor = factor)
            }
        } else {
            item {
                Text("Нет данных", color = Color.Gray)
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Логические показатели",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (!product.booleanFactors.isNullOrEmpty()) {
            items(product.booleanFactors) { factor ->
                BooleanFactorCard(factor = factor)
            }
        } else {
            item {
                Text("Нет данных", color = Color.Gray)
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Перечисляемые показатели",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (!product.enumFactors.isNullOrEmpty()) {
            items(product.enumFactors) { factor ->
                EnumFactorCard(factor = factor)
            }
        } else {
            item {
                Text("Нет данных", color = Color.Gray)
            }
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (imageUri != null) {
                Text("Изображение: $imageUri")
            } else {
                Text("Нет изображения")
            }
        }
    }
}

@Composable
fun ProductName(name: String?) {
    Text(
        text = name ?: "Неизвестный продукт",
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
                tint = if (rating != null && index < rating) Color(0xFFFFD700) else Color(0xFFE0E0E0),
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
fun NumericFactorCard(factor: NumericFactorDto) {
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
                text = factor.factorName ?: "Неизвестно",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Значение: ${factor.amount} ${factor.unitName ?: ""}",
                fontSize = 14.sp
            )
            if (factor.minValue != null && factor.maxValue != null) {
                Text(
                    text = "Норма: ${factor.minValue} - ${factor.maxValue} ${factor.unitName ?: ""}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun BooleanFactorCard(factor: BooleanFactorDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = factor.factorName ?: "Неизвестно",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = if (factor.value == true) "✓ Да" else "✗ Нет",
                fontSize = 14.sp,
                color = if (factor.value == true) Color.Green else Color.Red
            )
        }
        if (factor.impact != null && factor.impact != 0) {
            Text(
                text = "Влияние: ${factor.impact}",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun EnumFactorCard(factor: EnumFactorDto) {
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
                text = factor.factorName ?: "Неизвестно",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Значение: ${factor.enumValue ?: "Не указано"}",
                fontSize = 14.sp
            )
            if (factor.impact != null && factor.impact != 0) {
                Text(
                    text = "Влияние: ${factor.impact}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = value, fontSize = 16.sp, color = Color.Gray)
    }
}