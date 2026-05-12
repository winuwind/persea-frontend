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
import ru.persea.frontend.methods.encodeBase64

class SearchViewModel : ViewModel() {

    var suggestions by mutableStateOf<List<String>>(emptyList())
        private set

    var results by mutableStateOf<List<ProductDto>>(emptyList())
        private set

    private var searchJob: Job? = null

    fun onQueryChanged(query: String) {

        searchJob?.cancel()

        searchJob = viewModelScope.launch {

            delay(300)

            if (query.isBlank()) {
                suggestions = emptyList()
                return@launch
            }

            val encoded = encodeBase64(query)

            try {
                suggestions = ProductRetrofitClient.api.getSuggestions(encoded)
            } catch (e: Exception) {
                suggestions = emptyList()
            }
        }
    }

    fun onQuerySend(query: String) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {

            delay(300)

            if (query.isBlank()) {
                suggestions = emptyList()
                return@launch
            }

            val encoded = encodeBase64(query)

            try {
                results = ProductRetrofitClient.api.getProducts(
                    encoded,
                    null,
                    null,
                    null,
                    null,
                    1,
                    100,
                    "");
            } catch (e: Exception) {
                results = emptyList()
            }
        }
    }
}