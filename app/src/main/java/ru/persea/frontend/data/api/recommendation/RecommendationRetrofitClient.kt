package ru.persea.frontend.data.api.recommendation

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.persea.frontend.config.AppConfig
import ru.persea.frontend.data.api.auth.TokenAuthenticator
import ru.persea.frontend.data.api.users.AuthInterceptor

object RecommendationRetrofitClient {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val config = AppConfig.getInstance().recommendation

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .authenticator(TokenAuthenticator(appContext))
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(config.getBaseUrl())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RecommendationApiService by lazy {
        retrofit.create(RecommendationApiService::class.java)
    }
}