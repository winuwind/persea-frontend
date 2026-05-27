package ru.persea.frontend.data.api.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class TokenStorage(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("persea_prefs", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String) {
        Log.d("TokenStorage", "Saving tokens")
        prefs.edit().apply {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            apply()
        }
        TokenManager.accessToken = accessToken
    }

    fun getAccessToken(): String? {
        val token = prefs.getString("access_token", null)
        TokenManager.accessToken = token
        Log.d("TokenStorage", "getAccessToken: ${token != null}")
        return token
    }

    fun getRefreshToken(): String? {
        return prefs.getString("refresh_token", null)
    }

    fun clearTokens() {
        Log.d("TokenStorage", "Clearing tokens")
        prefs.edit().apply {
            remove("access_token")
            remove("refresh_token")
            apply()
        }
        TokenManager.accessToken = null
    }

    fun isLoggedIn(): Boolean {
        val loggedIn = getAccessToken() != null
        Log.d("TokenStorage", "isLoggedIn: $loggedIn")
        return loggedIn
    }
}