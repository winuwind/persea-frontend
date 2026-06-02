package ru.persea.frontend.data.api.users

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import ru.persea.frontend.data.api.auth.TokenManager
import ru.persea.frontend.data.api.auth.TokenStorage

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var token = TokenManager.accessToken

        val request = originalRequest.newBuilder()
            .apply {
                if (!token.isNullOrBlank()) {
                    addHeader("Authorization", "Bearer $token")
                }
            }
            .build()

        val response = chain.proceed(request)

        if (response.code == 401) {
            response.close()
            // Токен истек, нужно обновить
            // Возвращаем исходный запрос без токена
            val retryRequest = originalRequest.newBuilder()
                .header("Authorization", "")
                .build()
            return chain.proceed(retryRequest)
        }

        return response
    }
}