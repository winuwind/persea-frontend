package ru.persea.frontend.data.api.users

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.persea.frontend.config.AppConfig

object UserRetrofitClient {

    private val config = AppConfig.getInstance().user

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(config.getBaseUrl())
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: UserApiService = retrofit.create(UserApiService::class.java)
}