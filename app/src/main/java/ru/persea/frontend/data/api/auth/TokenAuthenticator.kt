package ru.persea.frontend.data.api.auth

import android.content.Context
import android.util.Log
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import kotlinx.coroutines.runBlocking

class TokenAuthenticator(private val context: Context) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val tokenStorage = TokenStorage(context)
        val refreshToken = tokenStorage.getRefreshToken()

        Log.d("TokenAuthenticator", "Attempting to refresh token")

        if (refreshToken.isNullOrBlank()) {
            Log.d("TokenAuthenticator", "No refresh token available")
            return null
        }

        return runBlocking {
            try {
                val newToken = AuthRetrofitClient.api.refreshToken(refreshToken = refreshToken)
                newToken.access_token?.let { accessToken ->
                    newToken.refresh_token?.let { newRefreshToken ->
                        tokenStorage.saveTokens(accessToken, newRefreshToken)
                        Log.d("TokenAuthenticator", "Token refreshed successfully")
                        response.request.newBuilder()
                            .header("Authorization", "Bearer $accessToken")
                            .build()
                    }
                } ?: run {
                    Log.d("TokenAuthenticator", "Failed to get new tokens")
                    null
                }
            } catch (e: Exception) {
                Log.e("TokenAuthenticator", "Error refreshing token: ${e.message}")
                null
            }
        }
    }
}