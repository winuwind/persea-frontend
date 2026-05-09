//package ru.persea.frontend.data.api.users
//
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//object UserRetrofitClient {
//
//    private const val BASE_URL = "http://10.0.2.2:8081"
//
//    private val tokenProvider = TokenProvider(context)
//    private val authInterceptor = AuthInterceptor(tokenProvider)
//
//    private val client = OkHttpClient.Builder()
//        .addInterceptor(
//            authInterceptor
//        )
//        .build()
//
//    val api: UserApiService = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .client(client)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create(UserApiService::class.java)
//}