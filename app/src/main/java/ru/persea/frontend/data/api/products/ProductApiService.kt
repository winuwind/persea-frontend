package ru.persea.frontend.data.api.products

import retrofit2.http.GET
import retrofit2.http.Query
import ru.persea.frontend.data.model.products.ProductDto

interface ProductApiService {

    @GET("/products/suggestions")
    suspend fun getSuggestions(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5
    ): List<String>

    @GET("/products")
    suspend fun getProducts(
        @Query("q") query: String?,
        @Query("category_id") category_id: Int?,
        @Query("brand_ids") brand_ids: Set<Int>?,
        @Query("min_rating") min_rating: Int?,
        @Query("maz_rating") max_rating: Int?,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): List<ProductDto>

    @GET("/products/{barcode}/barcode")
    suspend fun getProductsByBarcode(
        @Query("barcode") barcode: String
    ): ProductDto
}