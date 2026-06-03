package ru.persea.frontend.data.api.users

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import ru.persea.frontend.data.api.auth.TokenManager
import ru.persea.frontend.data.api.auth.TokenStorage
import org.json.JSONObject

class AuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val tokenStorage = TokenStorage(context)

        val token = tokenStorage.getAccessToken()

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
            val retryRequest = originalRequest.newBuilder()
                .header("Authorization", "")
                .build()
            return chain.proceed(retryRequest)
        }

        return response
    }

    companion object {
        private var cachedRoles: List<String>? = null
        private var lastTokenHash: Int? = null

        fun updateRoles(token: String?) {
            if (token.isNullOrBlank()) {
                cachedRoles = null
                lastTokenHash = null
                return
            }

            val currentHash = token.hashCode()
            if (lastTokenHash == currentHash && cachedRoles != null) {
                return
            }

            lastTokenHash = currentHash
            cachedRoles = extractRolesFromToken(token)
        }

        private fun extractRolesFromToken(token: String): List<String> {
            return try {
                val parts = token.split(".")
                if (parts.size == 3) {
                    val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
                    val json = JSONObject(payload)
                    val realmAccess = json.optJSONObject("realm_access")
                    val roles = realmAccess?.optJSONArray("roles")
                    val roleList = mutableListOf<String>()
                    if (roles != null) {
                        for (i in 0 until roles.length()) {
                            roles.optString(i)?.let { roleList.add(it) }
                        }
                    }
                    roleList
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }

        fun getUserRoles(): List<String> {
            val token = TokenManager.accessToken
            if (token.isNullOrBlank()) return emptyList()
            updateRoles(token)
            return cachedRoles ?: emptyList()
        }

        fun isAdmin(): Boolean = getUserRoles().contains("admin")

        fun isModerator(): Boolean = getUserRoles().contains("moderator") || isAdmin()

        fun clearRolesCache() {
            cachedRoles = null
            lastTokenHash = null
        }
    }
}