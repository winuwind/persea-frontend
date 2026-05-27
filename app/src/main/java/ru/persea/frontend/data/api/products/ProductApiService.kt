package ru.persea.frontend.data.api.products

import retrofit2.http.*
import ru.persea.frontend.data.model.products.*

interface ProductApiService {

    @GET("/products/suggestions")
    suspend fun getSuggestions(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5
    ): List<String>

    @GET("/products")
    suspend fun getProducts(
        @Query("q") query: String?,
        @Query("categoryId") categoryId: Long?,
        @Query("brandIds") brandIds: Set<Long>?,
        @Query("minRating") minRating: Int?,
        @Query("maxRating") maxRating: Int?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<ProductDto>

    @GET("/products/{id}")
    suspend fun getProductById(
        @Path("id") id: Long,
        @Query("include") include: String? = null
    ): ProductResponse

    @GET("/products/barcode/{barcode}")
    suspend fun getProductsByBarcode(
        @Path("barcode") barcode: String
    ): ProductDto

    @GET("/products/categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("/products/brands")
    suspend fun getBrands(): List<BrandDto>

    @GET("/factors")
    suspend fun getFactors(): List<Factor>

    @GET("/factors/units")
    suspend fun getUnits(): List<ru.persea.frontend.data.model.products.Unit>

    @GET("/factors/types")
    suspend fun getFactorTypes(): List<FactorType>

    @POST("/products")
    suspend fun createProduct(
        @Body request: CreateProductRequest
    ): ProductResponse

    @PUT("/products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Long,
        @Body request: CreateProductRequest
    ): ProductResponse

    @DELETE("/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long)

    @POST("/products/categories")
    suspend fun createCategory(@Body category: CategoryDto): CategoryDto

    @PUT("/products/categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Long,
        @Body category: CategoryDto
    ): CategoryDto

    @DELETE("/products/categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Long)

    @POST("/products/brands")
    suspend fun createBrand(@Body brand: BrandDto): BrandDto

    @PUT("/products/brands/{id}")
    suspend fun updateBrand(
        @Path("id") id: Long,
        @Body brand: BrandDto
    ): BrandDto

    @DELETE("/products/brands/{id}")
    suspend fun deleteBrand(@Path("id") id: Long)
}