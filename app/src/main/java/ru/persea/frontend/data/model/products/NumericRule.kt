package ru.persea.frontend.data.model.products

data class NumericRule(
    val id: Long?,
    val factorId: Long?,
    val categoryId: Long?,
    val unitId: Long?,
    val minValue: Double?,
    val maxValue: Double?
)