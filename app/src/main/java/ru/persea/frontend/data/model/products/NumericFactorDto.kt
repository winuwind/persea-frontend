package ru.persea.frontend.data.model.products

data class NumericFactorDto(
    val id: Long?,
    val factorId: Long?,
    val factorName: String?,
    val unitName: String?,
    val amount: Double?,
    val minValue: Double?,
    val maxValue: Double?
) {
    fun getSafeMinValue(): Double = minValue ?: 0.0
    fun getSafeMaxValue(): Double = if (maxValue == minValue) (maxValue ?: 100.0) + 1 else maxValue ?: 100.0
    fun getSafeAmount(): Double = amount ?: 0.0
}