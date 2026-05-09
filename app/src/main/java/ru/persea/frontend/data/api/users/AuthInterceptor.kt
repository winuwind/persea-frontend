package ru.persea.frontend.data.api.users

import okhttp3.Interceptor
import okhttp3.Response
import ru.persea.frontend.data.api.auth.TokenManager

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val token = TokenManager.accessToken

        val newRequest = originalRequest.newBuilder()
            .apply {

                if (!token.isNullOrBlank()) {
                    addHeader(
                        "Authorization",
                        "Bearer $token"
                    )
                }
            }
            .build()

        return chain.proceed(newRequest)
    }
}