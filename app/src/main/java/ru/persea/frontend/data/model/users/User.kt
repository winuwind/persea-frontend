package ru.persea.frontend.data.model.users

data class User(
        val id: String?,
        val username: String?,
        val email: String?,
        val roles: List<String>?
)