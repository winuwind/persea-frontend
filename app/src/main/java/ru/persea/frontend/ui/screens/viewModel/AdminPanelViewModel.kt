package ru.persea.frontend.ui.screens.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.persea.frontend.data.api.products.ProductRetrofitClient
import ru.persea.frontend.data.model.products.*
import ru.persea.frontend.data.model.products.Unit

class AdminPanelViewModel : ViewModel() {

    private val _factors = MutableStateFlow<List<Factor>>(emptyList())
    val factors: StateFlow<List<Factor>> = _factors.asStateFlow()

    private val _units = MutableStateFlow<List<Unit>>(emptyList())
    val units: StateFlow<List<Unit>> = _units.asStateFlow()

    private val _types = MutableStateFlow<List<FactorType>>(emptyList())
    val types: StateFlow<List<FactorType>> = _types.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun loadAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _factors.value = ProductRetrofitClient.api.getFactors()
                _units.value = ProductRetrofitClient.api.getUnits()
                _types.value = ProductRetrofitClient.api.getFactorTypes()
            } catch (e: Exception) {
                _message.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createFactor(name: String, typeId: Long, description: String?) {
        viewModelScope.launch {
            try {
                val request = CreateFactorRequest(name, typeId, description)
                val newFactor = ProductRetrofitClient.api.createFactor(request)
                _factors.value = _factors.value + newFactor
                _message.value = "Фактор создан: ${newFactor.name}"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun updateFactor(id: Long, name: String, typeId: Long, description: String?) {
        viewModelScope.launch {
            try {
                val request = CreateFactorRequest(name, typeId, description)
                val updated = ProductRetrofitClient.api.updateFactor(id, request)
                _factors.value = _factors.value.map { if (it.id == id) updated else it }
                _message.value = "Фактор обновлён: ${updated.name}"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun deleteFactor(id: Long) {
        viewModelScope.launch {
            try {
                ProductRetrofitClient.api.deleteFactor(id)
                _factors.value = _factors.value.filter { it.id != id }
                _message.value = "Фактор удалён"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun createUnit(name: String) {
        viewModelScope.launch {
            try {
                val request = CreateUnitRequest(name)
                val newUnit = ProductRetrofitClient.api.createUnit(request)
                _units.value = _units.value + newUnit
                _message.value = "Единица создана: ${newUnit.name}"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun updateUnit(id: Int, name: String) {
        viewModelScope.launch {
            try {
                val request = CreateUnitRequest(name)
                val updated = ProductRetrofitClient.api.updateUnit(id, request)
                _units.value = _units.value.map { if (it.id == id.toLong()) updated else it }
                _message.value = "Единица обновлена: ${updated.name}"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun deleteUnit(id: Int) {
        viewModelScope.launch {
            try {
                ProductRetrofitClient.api.deleteUnit(id)
                _units.value = _units.value.filter { it.id != id.toLong() }
                _message.value = "Единица удалена"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun createFactorType(name: String) {
        viewModelScope.launch {
            try {
                val request = CreateFactorTypeRequest(name)
                val newType = ProductRetrofitClient.api.createFactorType(request)
                _types.value = _types.value + newType
                _message.value = "Тип создан: ${newType.name}"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun updateFactorType(id: Int, name: String) {
        viewModelScope.launch {
            try {
                val request = CreateFactorTypeRequest(name)
                val updated = ProductRetrofitClient.api.updateFactorType(id, request)
                _types.value = _types.value.map { if (it.id == id.toLong()) updated else it }
                _message.value = "Тип обновлён: ${updated.name}"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun deleteFactorType(id: Int) {
        viewModelScope.launch {
            try {
                ProductRetrofitClient.api.deleteFactorType(id)
                _types.value = _types.value.filter { it.id != id.toLong() }
                _message.value = "Тип удалён"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}