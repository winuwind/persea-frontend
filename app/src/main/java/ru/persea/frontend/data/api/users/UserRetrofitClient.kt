package ru.persea.frontend.data.api.users

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserRetrofitClient {

    private const val BASE_URL =
        "http://10.0.2.2:8080"


    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {

            level = HttpLoggingInterceptor.Level.BODY
        }


    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .build()

    val api: UserApiService =
        retrofit.create(UserApiService::class.java)
}