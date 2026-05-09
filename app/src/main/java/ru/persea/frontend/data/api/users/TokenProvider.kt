//package ru.persea.frontend.data.api.users
//
//import android.content.Context
//import android.content.SharedPreferences
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//class TokenProvider(private val context: Context) {
//
//    private val sharedPreferences: SharedPreferences =
//        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
//
//    // Обычный метод без suspend для использования в Interceptor
//    fun getAccessToken(): String {
//        return sharedPreferences.getString("access_token", "") ?: ""
//    }
//
//    fun saveAccessToken(token: String) {
//        sharedPreferences.edit().putString("access_token", token).apply()
//    }
//
//    // Если нужны suspend версии для других мест
//    suspend fun getAccessTokenSuspend(): String = withContext(Dispatchers.IO) {
//        getAccessToken()
//    }
//
//    suspend fun saveAccessTokenSuspend(token: String) = withContext(Dispatchers.IO) {
//        saveAccessToken(token)
//    }
//}