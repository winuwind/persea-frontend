package ru.persea.frontend.utils

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

object PKCEHelper {
    fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val codeVerifier = ByteArray(64)
        secureRandom.nextBytes(codeVerifier)
        return Base64.encodeToString(codeVerifier, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }

    fun generateCodeChallenge(codeVerifier: String): String {
        val bytes = codeVerifier.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(bytes)
        return Base64.encodeToString(digest, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }
}