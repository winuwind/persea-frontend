package ru.persea.frontend.data.model.products

//import java.util.List

data class ProductDto(
    val id: Long?,
    val name: String?,
    val rating: Int?,
    val imageURI: String?,
    val factors: List<FactorDto>?
)