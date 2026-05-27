package ru.persea.frontend.ui.screens.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.persea.frontend.data.api.products.ProductRetrofitClient
import ru.persea.frontend.data.model.products.Factor
import ru.persea.frontend.data.model.products.FactorType
import ru.persea.frontend.data.model.products.Unit

class FactorsViewModel : ViewModel() {

    var factors by mutableStateOf<List<Factor>>(emptyList())
        private set
    var units by mutableStateOf<List<Unit>>(emptyList())
        private set
    var types by mutableStateOf<List<FactorType>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set

    fun loadFactors() {
        viewModelScope.launch {
            isLoading = true
            try {
                factors = ProductRetrofitClient.api.getFactors()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun loadUnits() {
        viewModelScope.launch {
            try {
                units = ProductRetrofitClient.api.getUnits()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadTypes() {
        viewModelScope.launch {
            try {
                types = ProductRetrofitClient.api.getFactorTypes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}