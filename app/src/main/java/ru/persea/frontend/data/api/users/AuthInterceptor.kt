//package ru.persea.frontend.data.api.users
//
//import okhttp3.Interceptor
//import okhttp3.Response
//import javax.inject.Inject
//
//class AuthInterceptor @Inject constructor(
//    private val tokenProvider: TokenProvider
//) : Interceptor {
//
//    // Убираем suspend - метод intercept не suspend в OkHttp
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val originalRequest = chain.request()
//
//        // Получаем актуальный access token (без suspend, используем обычный метод)
//        val accessToken = tokenProvider.getAccessToken()
//
//        // Добавляем токен в заголовок Authorization
//        val authenticatedRequest = originalRequest.newBuilder()
//            .header("Authorization", "Bearer $accessToken")
//            .build()
//
//        // Выполняем запрос с токеном
//        return chain.proceed(authenticatedRequest)
//    }
//}