//package ru.persea.frontend.data.api.users
//
//import retrofit2.Response
//import retrofit2.http.GET
//import retrofit2.http.Path
//import retrofit2.http.Query
//import ru.persea.frontend.data.model.Product
//
//interface UserApiService {
//
//    @GET("users/me/viewed-products")
//    suspend fun getMyViewedProducts(
//        @Query("page") page: Int,
//        @Query("size") size: Int,
//        @Query("sort") sort: String? = null
//    ): Response<PagedResponse<ProductViewHistory>>
//
//
//    @GET("users/{userId}/viewed-products")
//    suspend fun getUserViewedProducts(
//        @Path("userId") userId: String,
//        @Query("page") page: Int,
//        @Query("size") size: Int,
//        @Query("sort") sort: String? = null
//    ): Response<PagedResponse<ProductViewHistory>>
//
//
//    @GET("users/me/scanned-products")
//    suspend fun getMyScannedProducts(
//        @Query("page") page: Int,
//        @Query("size") size: Int,
//        @Query("sort") sort: String? = null
//    ): Response<PagedResponse<ProductScanHistory>>
//
//
//    @GET("users/{userId}/scanned-products")
//    suspend fun getUserScannedProducts(
//        @Path("userId") userId: String,
//        @Query("page") page: Int,
//        @Query("size") size: Int,
//        @Query("sort") sort: String? = null
//    ): Response<PagedResponse<ProductScanHistory>>
//}