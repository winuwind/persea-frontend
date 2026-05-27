package ru.persea.frontend.data.model.users

data class LoginResponse(
    val access_token: String?,
    val expires_in: Long?,
    val refresh_expires_in: Long?,
    val refresh_token: String?,
    val token_type: String?,
    val session_state: String?,
    val scope: String?
)