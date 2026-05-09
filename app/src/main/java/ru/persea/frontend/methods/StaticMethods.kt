package ru.persea.frontend.methods

import android.util.Base64

fun encodeBase64(input: String): String {
    return Base64.encodeToString(
        input.toByteArray(),
        Base64.NO_WRAP
    )
}