package ru.persea.frontend.ui.screens.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.persea.frontend.data.api.products.ProductRetrofitClient
import ru.persea.frontend.data.api.recommendation.RecommendationRetrofitClient
import ru.persea.frontend.data.model.products.BrandDto
import ru.persea.frontend.data.model.products.CategoryDto

class StudioViewModel : ViewModel() {

    private val _brandName = MutableStateFlow("")
    val brandName: StateFlow<String> = _brandName.asStateFlow()

    private val _brandDescription = MutableStateFlow("")
    val brandDescription: StateFlow<String> = _brandDescription.asStateFlow()

    private val _categoryName = MutableStateFlow("")
    val categoryName: StateFlow<String> = _categoryName.asStateFlow()

    private val _categoryCode = MutableStateFlow("")
    val categoryCode: StateFlow<String> = _categoryCode.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasModeratorRole = MutableStateFlow(false)
    val hasModeratorRole: StateFlow<Boolean> = _hasModeratorRole.asStateFlow()

    private val _hasAdminRole = MutableStateFlow(false)
    val hasAdminRole: StateFlow<Boolean> = _hasAdminRole.asStateFlow()

    private val _checkingRoles = MutableStateFlow(true)
    val checkingRoles: StateFlow<Boolean> = _checkingRoles.asStateFlow()

    fun updateBrandName(value: String) {
        _brandName.value = value
    }

    fun updateBrandDescription(value: String) {
        _brandDescription.value = value
    }

    fun updateCategoryName(value: String) {
        _categoryName.value = value
    }

    fun updateCategoryCode(value: String) {
        _categoryCode.value = value.uppercase()
    }

    fun setRoles(moderator: Boolean, admin: Boolean) {
        _hasModeratorRole.value = moderator
        _hasAdminRole.value = admin
        _checkingRoles.value = false
    }

    fun setCheckingRoles(value: Boolean) {
        _checkingRoles.value = value
    }

    fun createBrand() {
        val name = _brandName.value
        if (name.isBlank()) {
            _message.value = "Введите название бренда"
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val brand = ProductRetrofitClient.api.createBrand(
                    BrandDto(id = null, name = name, description = _brandDescription.value.ifBlank { null })
                )
                _message.value = "Бренд создан: ${brand.name}"
                _brandName.value = ""
                _brandDescription.value = ""
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createCategory() {
        val name = _categoryName.value
        val code = _categoryCode.value
        if (name.isBlank()) {
            _message.value = "Введите название категории"
            return
        }
        if (code.isBlank()) {
            _message.value = "Введите код категории"
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val category = ProductRetrofitClient.api.createCategory(
                    CategoryDto(id = null, name = name, code = code)
                )
                _message.value = "Категория создана: ${category.name}"
                _categoryName.value = ""
                _categoryCode.value = ""
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun recalculateRecommendations() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                RecommendationRetrofitClient.api.recalculateRecommendations()
                _message.value = "Пересчет рекомендаций запущен"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}