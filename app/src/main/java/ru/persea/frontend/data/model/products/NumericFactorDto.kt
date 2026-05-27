package ru.persea.frontend.data.model.products

data class NumericFactorDto(
    val id: Long?,
    val factorId: Long?,
    val factorName: String?,
    val unitName: String?,
    val amount: Double?,
    val minValue: Double?,
    val maxValue: Double?
)