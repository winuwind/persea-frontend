package ru.persea.frontend.data.api.recommendation

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.persea.frontend.config.AppConfig
import ru.persea.frontend.data.api.users.AuthInterceptor

object RecommendationRetrofitClient {

    private val config = AppConfig.getInstance().recommendation

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(config.getBaseUrl())
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: RecommendationApiService = retrofit.create(RecommendationApiService::class.java)
}