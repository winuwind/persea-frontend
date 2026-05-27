package ru.persea.frontend.data.model.products

data class BooleanFactorDto(
    val id: Long?,
    val factorId: Long?,
    val factorName: String?,
    val value: Boolean?,
    val impact: Int?
)