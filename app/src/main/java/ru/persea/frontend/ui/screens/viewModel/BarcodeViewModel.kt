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

    fun searchByBarcode(code: String, onComplete: (ProductDto?) -> Unit = {}) {
        if (code.isBlank()) {
            onComplete(null)
            return
        }

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            isLoading = true
            product = null

            try {
                val result = ProductRetrofitClient.api.getProductsByBarcode(code)
                product = result
                isLoading = false
                onComplete(result)
            } catch (e: Exception) {
                product = null
                isLoading = false
                onComplete(null)
            }
        }
    }
}