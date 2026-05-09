package ru.persea.frontend.data.api.users

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.persea.frontend.data.model.products.ProductDto

interface UserApiService {

    /** ********************
     *  For simple user
     */

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


    /** *********************
     * For admins
     */

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