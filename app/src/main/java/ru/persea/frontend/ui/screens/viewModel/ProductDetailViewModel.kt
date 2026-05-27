package ru.persea.frontend.ui.screens.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.persea.frontend.data.api.products.ProductRetrofitClient
import ru.persea.frontend.data.model.products.ProductResponse
import ru.persea.frontend.data.api.users.UserRetrofitClient

class ProductDetailViewModel : ViewModel() {

    private val _product = MutableStateFlow<ProductResponse?>(null)
    val product: StateFlow<ProductResponse?> = _product

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun loadProduct(productId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                _product.value = ProductRetrofitClient.api.getProductById(
                    id = productId,
                    include = "FACTORS"
                )
                checkFavoriteStatus(productId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun checkFavoriteStatus(productId: Long) {
        viewModelScope.launch {
            try {
                val favorites = UserRetrofitClient.api.getMyFavorites(page = 0, size = 100)
                _isFavorite.value = favorites.any { it.id == productId }
            } catch (e: Exception) {
                _isFavorite.value = false
            }
        }
    }

    fun toggleFavorite(productId: Long, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                if (_isFavorite.value) {
                    UserRetrofitClient.api.removeFromFavorites(productId)
                    _isFavorite.value = false
                    onComplete(false)
                } else {
                    UserRetrofitClient.api.addToFavorites(productId)
                    _isFavorite.value = true
                    onComplete(true)
                }
            } catch (e: Exception) {
                onComplete(_isFavorite.value)
            }
        }
    }
}