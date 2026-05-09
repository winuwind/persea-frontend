package ru.persea.frontend.data.model.users

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class ProductDto(
    val id: Long?,
    val name: String?,
    val rating: Int?,
    @JsonProperty("image_uri") val imageURI: String?,
    val createdAt: Instant?
)