package ru.persea.frontend.data.model.products

enum class ProductInclude(private val param: String) {
    FACTORS("factors"),
    DESCRIPTION("description"),
    REVIEWS("reviews");

    fun getParam(): String = param
}