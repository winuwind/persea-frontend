package ru.persea.frontend.data.api.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.runBlocking

class TokenStorage(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("persea_prefs", Context.MODE_PRIVATE)

    var onRefreshTokenNeeded: (() -> Boolean)? = null

    fun saveTokens(accessToken: String, refreshToken: String) {
        Log.d("TokenStorage", "Saving tokens")

        val accessExpiry = extractExpiryFromToken(accessToken)
        val refreshExpiry = extractExpiryFromToken(refreshToken)

        prefs.edit().apply {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            putLong("access_token_expiry", accessExpiry)
            putLong("refresh_token_expiry", refreshExpiry)
            apply()
        }
        TokenManager.accessToken = accessToken
        // Обновляем кеш ролей
        ru.persea.frontend.data.api.users.AuthInterceptor.updateRoles(accessToken)
    }

    fun getAccessToken(): String? {
        var token = prefs.getString("access_token", null)

        if (token != null && isTokenExpired("access_token_expiry")) {
            Log.d("TokenStorage", "Access token expired, trying to refresh")

            val refreshed = onRefreshTokenNeeded?.invoke() ?: false

            if (refreshed) {
                token = prefs.getString("access_token", null)
                Log.d("TokenStorage", "Token refreshed successfully")
                // Обновляем кеш ролей для нового токена
                ru.persea.frontend.data.api.users.AuthInterceptor.updateRoles(token)
            } else {
                Log.d("TokenStorage", "Token refresh failed, logging out")
                clearTokens()
                return null
            }
        }

        TokenManager.accessToken = token
        Log.d("TokenStorage", "getAccessToken: ${token != null}")
        return token
    }

    fun getRefreshToken(): String? {
        val token = prefs.getString("refresh_token", null)

        if (token != null && isTokenExpired("refresh_token_expiry")) {
            Log.d("TokenStorage", "Refresh token expired")
            clearTokens()
            return null
        }

        return token
    }

    private fun extractExpiryFromToken(token: String): Long {
        return try {
            val parts = token.split(".")
            if (parts.size == 3) {
                val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
                val json = org.json.JSONObject(payload)
                val exp = json.optLong("exp", 0)
                exp * 1000
            } else {
                0
            }
        } catch (e: Exception) {
            Log.e("TokenStorage", "Error extracting expiry", e)
            0
        }
    }

    private fun isTokenExpired(expiryKey: String): Boolean {
        val expiry = prefs.getLong(expiryKey, 0)
        if (expiry == 0L) return false
        return System.currentTimeMillis() >= expiry
    }

    fun clearTokens() {
        Log.d("TokenStorage", "Clearing tokens")
        prefs.edit().apply {
            remove("access_token")
            remove("refresh_token")
            remove("access_token_expiry")
            remove("refresh_token_expiry")
            apply()
        }
        TokenManager.accessToken = null
    }

    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }
}