package ru.persea.frontend.data.model.products

data class CreateProductRequest(
    val name: String?,
    val categoryId: Long?,
    val brandId: Long?,
    val imageURI: String?,
    val barcode: String?,
    val numericFactors: List<NumericFactorInput>?,
    val booleanFactors: List<BooleanFactorInput>?,
    val enumFactors: List<EnumFactorInput>?
)

data class NumericFactorInput(
    val factorId: Long?,
    val amount: Double?
)

data class BooleanFactorInput(
    val factorId: Long?,
    val value: Boolean?
)

data class EnumFactorInput(
    val factorId: Long?,
    val enumValueId: Long?
)