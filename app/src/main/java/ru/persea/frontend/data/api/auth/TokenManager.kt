package ru.persea.frontend.data.api.auth

import ru.persea.frontend.data.api.users.AuthInterceptor

object TokenManager {
    var accessToken: String? = null
        set(value) {
            field = value
            AuthInterceptor.updateRoles(value)
            onTokenChanged?.invoke(value)
        }

    var onTokenChanged: ((String?) -> Unit)? = null
}