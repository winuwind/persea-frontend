package ru.persea.frontend.ui.screens.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
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

    init {
        isLoggedIn = tokenStorage.isLoggedIn()
        Log.d("AuthViewModel", "isLoggedIn: $isLoggedIn")
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

    fun refreshToken(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val refreshToken = tokenStorage.getRefreshToken()
            if (refreshToken.isNullOrBlank()) {
                onComplete(false)
                return@launch
            }

            try {
                val response = AuthRetrofitClient.api.refreshToken(refreshToken = refreshToken)
                response.access_token?.let { accessToken ->
                    response.refresh_token?.let { newRefreshToken ->
                        tokenStorage.saveTokens(accessToken, newRefreshToken)
                        isLoggedIn = true
                        onComplete(true)
                        return@launch
                    }
                }
                onComplete(false)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    fun logout() {
        tokenStorage.clearTokens()
        isLoggedIn = false
    }
}