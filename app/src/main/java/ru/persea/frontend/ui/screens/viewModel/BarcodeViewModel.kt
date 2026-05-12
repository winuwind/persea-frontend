package ru.persea.frontend.ui.screens.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.persea.frontend.data.api.products.ProductRetrofitClient
import ru.persea.frontend.data.model.products.ProductDto

class BarcodeViewModel : ViewModel() {
    var product by mutableStateOf<ProductDto?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    private var searchJob: Job? = null

    // Callback для уведомления о завершении поиска
    private var onProductFoundCallback: ((ProductDto?) -> Unit)? = null

    fun searchByBarcode(code: String, onComplete: (ProductDto?) -> Unit = {}) {
        onProductFoundCallback = onComplete

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(300)
            isLoading = true

            if (code.isBlank()) {
                product = null
                isLoading = false
                onComplete(null)
                return@launch
            }

            try {
                product = ProductRetrofitClient.api.getProductsByBarcode(code)
                isLoading = false
                onComplete(product)
            } catch (e: Exception) {
                product = null
                isLoading = false
                onComplete(null)
            }
        }
    }

    // Альтернативный метод с suspend функцией
    suspend fun searchByBarcodeSuspend(code: String): ProductDto? {
        return try {
            delay(300)
            if (code.isBlank()) null
            else ProductRetrofitClient.api.getProductsByBarcode(code)
        } catch (e: Exception) {
            null
        }
    }
}