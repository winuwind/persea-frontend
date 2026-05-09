package ru.persea.frontend.data.model.users

import java.util.UUID

data class UserActionEvent(
    val userId: UUID?,
    val action: String?
)