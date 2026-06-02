package ru.persea.frontend.data.api.products

import retrofit2.http.*
import ru.persea.frontend.data.model.products.*
import ru.persea.frontend.data.model.products.Unit

interface ProductApiService {

    // Existing methods
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
    suspend fun getUnits(): List<Unit>

    @GET("/factors/types")
    suspend fun getFactorTypes(): List<FactorType>

    // Factor management
    @POST("/factors")
    suspend fun createFactor(@Body request: CreateFactorRequest): Factor

    @PUT("/factors/{factorId}")
    suspend fun updateFactor(
        @Path("factorId") factorId: Long,
        @Body request: CreateFactorRequest
    ): Factor

    @DELETE("/factors/{factorId}")
    suspend fun deleteFactor(@Path("factorId") factorId: Long)

    // Unit management
    @POST("/factors/units")
    suspend fun createUnit(@Body request: CreateUnitRequest): Unit

    @PUT("/factors/units/{id}")
    suspend fun updateUnit(
        @Path("id") id: Int,
        @Body request: CreateUnitRequest
    ): Unit

    @DELETE("/factors/units/{id}")
    suspend fun deleteUnit(@Path("id") id: Int)

    // Factor type management
    @POST("/factors/types")
    suspend fun createFactorType(@Body request: CreateFactorTypeRequest): FactorType

    @PUT("/factors/types/{id}")
    suspend fun updateFactorType(
        @Path("id") id: Int,
        @Body request: CreateFactorTypeRequest
    ): FactorType

    @DELETE("/factors/types/{id}")
    suspend fun deleteFactorType(@Path("id") id: Int)

    // Numeric rules
    @POST("/factors/{factorId}/numeric-rules")
    suspend fun createNumericRule(
        @Path("factorId") factorId: Long,
        @Body request: CreateNumericRuleRequest
    ): NumericRuleResponse

    @PUT("/factors/numeric-rules/{ruleId}")
    suspend fun updateNumericRule(
        @Path("ruleId") ruleId: Long,
        @Body request: CreateNumericRuleRequest
    ): NumericRuleResponse

    @DELETE("/factors/numeric-rules/{ruleId}")
    suspend fun deleteNumericRule(@Path("ruleId") ruleId: Long)

    // Boolean rules
    @POST("/factors/{factorId}/boolean-rules")
    suspend fun createBooleanRule(
        @Path("factorId") factorId: Long,
        @Body request: CreateBooleanRuleRequest
    ): BooleanRuleResponse

    @PUT("/factors/boolean-rules/{ruleId}")
    suspend fun updateBooleanRule(
        @Path("ruleId") ruleId: Long,
        @Body request: CreateBooleanRuleRequest
    ): BooleanRuleResponse

    @DELETE("/factors/boolean-rules/{ruleId}")
    suspend fun deleteBooleanRule(@Path("ruleId") ruleId: Long)

    // Enum values
    @POST("/factors/{factorId}/enum-values")
    suspend fun createEnumValue(
        @Path("factorId") factorId: Long,
        @Body request: CreateEnumValueRequest
    ): EnumValueResponse

    @PUT("/factors/enum-values/{valueId}")
    suspend fun updateEnumValue(
        @Path("valueId") valueId: Long,
        @Body request: CreateEnumValueRequest
    ): EnumValueResponse

    @DELETE("/factors/enum-values/{valueId}")
    suspend fun deleteEnumValue(@Path("valueId") valueId: Long)

    // Enum rules
    @POST("/factors/enum-values/{valueId}/enum-rules")
    suspend fun createEnumRule(
        @Path("valueId") valueId: Long,
        @Body request: CreateEnumRuleRequest
    ): EnumRuleResponse

    @PUT("/factors/enum-rules/{ruleId}")
    suspend fun updateEnumRule(
        @Path("ruleId") ruleId: Long,
        @Body request: CreateEnumRuleRequest
    ): EnumRuleResponse

    @DELETE("/factors/enum-rules/{ruleId}")
    suspend fun deleteEnumRule(@Path("ruleId") ruleId: Long)

    // Product management (existing)
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