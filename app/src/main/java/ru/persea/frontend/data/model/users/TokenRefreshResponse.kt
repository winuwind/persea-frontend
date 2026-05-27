package ru.persea.frontend.data.model.users

data class TokenRefreshResponse(
    val access_token: String?,
    val expires_in: Long?,
    val refresh_expires_in: Long?,
    val refresh_token: String?,
    val token_type: String?,
    val scope: String?
)