package ru.persea.frontend.data.model.recommendation

import ru.persea.frontend.data.model.products.ProductDto
import java.time.Instant

data class RecommendationFeed(
    val userId: String?,
    val items: List<RecommendationItem>?,
    val generatedAt: Instant?
)

data class RecommendationItem(
    val product: ProductDto?,
    val score: Double?,
    val reason: String?
)