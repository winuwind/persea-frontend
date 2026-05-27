package ru.persea.frontend.data.api.users

import retrofit2.http.*
import ru.persea.frontend.data.model.users.FavoriteProduct
import ru.persea.frontend.data.model.users.ProductDto

interface UserApiService {

    @GET("/users/me/viewed-products")
    suspend fun getMyViewedProducts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): List<ProductDto>

    @GET("/users/me/scanned-products")
    suspend fun getMyScannedProducts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): List<ProductDto>

    @GET("/users/me/favorites")
    suspend fun getMyFavorites(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): List<FavoriteProduct>

    @POST("/users/me/favorites/{productId}")
    suspend fun addToFavorites(@Path("productId") productId: Long)

    @DELETE("/users/me/favorites/{productId}")
    suspend fun removeFromFavorites(@Path("productId") productId: Long)

    @GET("/users/{userId}/viewed-products")
    suspend fun getUserViewedProducts(
        @Path("userId") userId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): List<ProductDto>

    @GET("/users/{userId}/scanned-products")
    suspend fun getUserScannedProducts(
        @Path("userId") userId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): List<ProductDto>
}