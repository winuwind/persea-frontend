package ru.persea.frontend.ui.screens.auth

import android.webkit.WebView
import android.webkit.WebViewClient
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import ru.persea.frontend.config.AppConfig
import ru.persea.frontend.utils.PKCEHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthWebViewScreen(
    navController: NavController,
    onAuthSuccess: (String, String) -> Unit
) {
    val context = LocalContext.current
    val config = remember { AppConfig.getInstance() }
    val authConfig = config.auth

    val codeVerifier = remember { PKCEHelper.generateCodeVerifier() }
    val codeChallenge = remember { PKCEHelper.generateCodeChallenge(codeVerifier) }

    val authUrl = "${authConfig.protocol}://${authConfig.host}:${authConfig.port}/realms/persea/protocol/openid-connect/auth?" +
            "client_id=android-app&" +
            "redirect_uri=https://oauth.pstmn.io/v1/callback&" +
            "response_type=code&" +
            "scope=openid%20profile%20email&" +
            "code_challenge=$codeChallenge&" +
            "code_challenge_method=S256"

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Вход") })
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                            }

                            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                                url?.let {
                                    if (it.startsWith("https://oauth.pstmn.io/v1/callback")) {
                                        val uri = Uri.parse(it)
                                        val code = uri.getQueryParameter("code")
                                        code?.let { authCode ->
                                            onAuthSuccess(authCode, codeVerifier)
                                        }
                                        return true
                                    }
                                }
                                return super.shouldOverrideUrlLoading(view, url)
                            }
                        }
                        loadUrl(authUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}