package ru.persea.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.persea.frontend.data.model.products.NumericFactorDto
import java.text.DecimalFormat

private fun compactAmount(value: Double?): String {
    if (value == null) return "?"
    val df = DecimalFormat("#.###")
    return df.format(value).trimEnd('0').trimEnd('.')
}

private fun isNumericConcern(factor: NumericFactorDto): Boolean {
    val min = factor.minValue ?: return false
    val max = factor.maxValue ?: return false
    val amount = factor.amount ?: return false
    if (max == min) return amount != max
    val width = max - min
    val lowerBuffer = min + width * 0.12
    val upperBuffer = max - width * 0.12
    if (min == 0.0) return amount > upperBuffer
    return amount < lowerBuffer || amount > upperBuffer
}

@Composable
fun FactorGauge(factor: NumericFactorDto) {
    val min = factor.minValue ?: 0.0
    val max = factor.maxValue ?: 100.0
    val amount = factor.amount ?: 0.0

    // Защита от деления на ноль
    val range = if (max == min) 1.0 else max - min
    val raw = ((amount - min) / range).coerceIn(0.0, 1.0)

    val concern = isNumericConcern(factor)
    val showMarker = amount >= min && amount <= max

    val density = LocalDensity.current

    Card(
        modifier = Modifier.fillMaxWidth(),
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
            // Первая строка: название и значение
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = factor.factorName ?: "Неизвестно",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = if (concern) "⚠️ На границе нормы" else "✅ В пределах нормы",
                        fontSize = 12.sp,
                        color = if (concern) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        maxLines = 1
                    )
                }

                Text(
                    text = "${compactAmount(amount)} ${factor.unitName ?: ""}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Шкала с позицией маркера
            var markerPosition by remember { mutableStateOf(0.dp) }
            var boxWidth by remember { mutableStateOf(0.dp) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        val widthPx = coordinates.size.width.toFloat()
                        val positionPx = (raw * widthPx).toFloat()
                        markerPosition = with(density) { positionPx.toDp() }
                    }
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF4CAF50),
                                Color(0xFFFFC107),
                                Color(0xFFF44336)
                            ),
                            start = Offset.Zero,
                            end = Offset(1000f, 0f)
                        )
                    )
            ) {
                if (showMarker) {
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .height(14.dp)
                            .align(Alignment.CenterStart)
                            .offset(x = markerPosition - 3.dp)
                            .background(Color.White, RoundedCornerShape(2.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Подписи шкалы
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = compactAmount(min),
                    fontSize = 10.sp,
                    color = Color.Gray
                )
                Text(
                    text = "норма",
                    fontSize = 10.sp,
                    color = Color(0xFFFFC107)
                )
                Text(
                    text = compactAmount(max),
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}