package ru.persea.frontend.ui.screens.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.persea.frontend.data.api.auth.AuthRetrofitClient
import ru.persea.frontend.data.api.auth.TokenStorage

class AuthViewModel(private val context: Context) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var isLoggedIn by mutableStateOf(false)
        private set

    private val tokenStorage = TokenStorage(context)
    private var onLogoutCallback: (() -> Unit)? = null

    init {
        // Устанавливаем колбэк для обновления токена (синхронный)
        tokenStorage.onRefreshTokenNeeded = {
            refreshTokenSync()
        }

        isLoggedIn = tokenStorage.isLoggedIn()
        Log.d("AuthViewModel", "isLoggedIn: $isLoggedIn")
    }

    fun setOnLogoutCallback(callback: () -> Unit) {
        onLogoutCallback = callback
    }

    fun exchangeCodeForToken(code: String, codeVerifier: String, onSuccess: () -> Unit) {
        Log.d("AuthViewModel", "exchangeCodeForToken called with code: $code")

        viewModelScope.launch {
            isLoading = true
            error = null

            try {
                Log.d("AuthViewModel", "Calling token endpoint")

                val response = AuthRetrofitClient.api.getToken(
                    grantType = "authorization_code",
                    code = code,
                    codeVerifier = codeVerifier,
                    redirectUri = "https://oauth.pstmn.io/v1/callback"
                )

                Log.d("AuthViewModel", "Response received: access_token=${response.access_token != null}")

                response.access_token?.let { accessToken ->
                    response.refresh_token?.let { refreshToken ->
                        tokenStorage.saveTokens(accessToken, refreshToken)
                        isLoggedIn = true
                        Log.d("AuthViewModel", "Tokens saved successfully")
                        onSuccess()
                    }
                } ?: run {
                    error = "Ошибка авторизации: токен не получен"
                    Log.e("AuthViewModel", "No access token in response")
                }
            } catch (e: Exception) {
                error = "Ошибка соединения: ${e.message}"
                Log.e("AuthViewModel", "Exception: ${e.message}", e)
            } finally {
                isLoading = false
            }
        }
    }

    // Синхронная функция для обновления токена (для вызова из колбэка)
    private fun refreshTokenSync(): Boolean {
        return try {
            val refreshToken = tokenStorage.getRefreshToken()
            if (refreshToken.isNullOrBlank()) {
                return false
            }

            // Используем runBlocking для вызова suspend функции
            runBlocking {
                try {
                    val response = AuthRetrofitClient.api.refreshToken(refreshToken = refreshToken)
                    response.access_token?.let { accessToken ->
                        response.refresh_token?.let { newRefreshToken ->
                            tokenStorage.saveTokens(accessToken, newRefreshToken)
                            isLoggedIn = true
                            return@runBlocking true
                        }
                    }
                    return@runBlocking false
                } catch (e: Exception) {
                    Log.e("AuthViewModel", "Token refresh failed", e)
                    return@runBlocking false
                }
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Token refresh exception", e)
            false
        }
    }

    suspend fun refreshTokenSuspending(): Boolean {
        return try {
            val refreshToken = tokenStorage.getRefreshToken()
            if (refreshToken.isNullOrBlank()) {
                return false
            }

            val response = AuthRetrofitClient.api.refreshToken(refreshToken = refreshToken)
            response.access_token?.let { accessToken ->
                response.refresh_token?.let { newRefreshToken ->
                    tokenStorage.saveTokens(accessToken, newRefreshToken)
                    isLoggedIn = true
                    return@let true
                }
            } ?: false
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Token refresh failed", e)
            false
        }
    }

    fun refreshToken(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = refreshTokenSuspending()
            onComplete(result)
            if (!result) {
                logout()
            }
        }
    }

    fun logout() {
        Log.d("AuthViewModel", "Logging out")
        tokenStorage.clearTokens()
        isLoggedIn = false
        clearWebViewCookies()
        onLogoutCallback?.invoke()
    }

    private fun clearWebViewCookies() {
        try {
            android.webkit.CookieManager.getInstance().removeAllCookies(null)
            android.webkit.CookieManager.getInstance().flush()
            Log.d("AuthViewModel", "WebView cookies cleared")
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Error clearing cookies", e)
        }
    }
}