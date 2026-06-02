package ru.persea.frontend.data.model.products

data class ProductResponse(
    val id: Long?,
    val name: String?,
    val brand: BrandDto?,
    val category: CategoryDto?,
    val rating: Int?,
    val imageURI: String?,
    val barcode: String?,
    val numericFactors: List<NumericFactorDto>?,
    val booleanFactors: List<BooleanFactorDto>?,
    val enumFactors: List<EnumFactorDto>?
)