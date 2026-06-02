package ru.persea.frontend.data.api.recommendation

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.persea.frontend.data.model.products.ProductDto
import ru.persea.frontend.data.model.recommendation.RecommendationFeed

interface RecommendationApiService {

    @GET("/recommendation/feed/me")
    suspend fun getRecommendationFeed(
        @Query("limit") limit: Int = 10
    ): List<ProductDto>

    @POST("/recommendation/recalculate")
    suspend fun recalculateRecommendations()
}