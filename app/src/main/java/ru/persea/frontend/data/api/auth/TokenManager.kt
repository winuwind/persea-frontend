package ru.persea.frontend.data.api.auth

object TokenManager {
    var accessToken: String? = null
        set(value) {
            field = value
            onTokenChanged?.invoke(value)
        }

    var onTokenChanged: ((String?) -> Unit)? = null
}