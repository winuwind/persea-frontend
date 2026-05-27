package ru.persea.frontend.data.api.auth

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.persea.frontend.config.AppConfig
import android.util.Log

object AuthRetrofitClient {

    private val config = AppConfig.getInstance().auth

    init {
        Log.d("AuthRetrofitClient", "Auth URL: ${config.getFullUrl()}/")
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("${config.getFullUrl()}/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: AuthApiService = retrofit.create(AuthApiService::class.java)
}