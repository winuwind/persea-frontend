package ru.persea.frontend.ui.screens.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.persea.frontend.data.model.products.ProductDto

class ProductDetailViewModel : ViewModel() {

    private val _product = MutableStateFlow<ProductDto?>(null)
    val product: StateFlow<ProductDto?> = _product

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadProduct(productId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Здесь загрузите продукт из репозитория
                // val product = repository.getProductById(productId)
                // _product.value = product

                // Временная заглушка для тестирования
                _product.value = ProductDto(
                    id = productId,
                    name = "Sample Product",
                    rating = 4,
                    imageURI = null,
                    factors = emptyList()
                )
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}