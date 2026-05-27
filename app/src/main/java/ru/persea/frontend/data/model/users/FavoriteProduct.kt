package ru.persea.frontend.data.model.users

import ru.persea.frontend.data.model.products.BrandDto
import ru.persea.frontend.data.model.products.CategoryDto

data class FavoriteProduct(
    val id: Long?,
    val name: String?,
    val brand: BrandDto?,
    val category: CategoryDto?,
    val rating: Int?,
    val imageURI: String?
)