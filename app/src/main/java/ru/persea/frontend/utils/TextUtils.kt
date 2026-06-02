package ru.persea.frontend.utils

import android.util.Base64

object TextUtils {

    fun toBase64(input: String): String {
        return Base64.encodeToString(
            input.toByteArray(Charsets.UTF_8),
            Base64.NO_WRAP or Base64.URL_SAFE
        )
    }

    fun fromBase64(input: String): String {
        return try {
            String(Base64.decode(input, Base64.NO_WRAP or Base64.URL_SAFE), Charsets.UTF_8)
        } catch (e: Exception) {
            input
        }
    }
}