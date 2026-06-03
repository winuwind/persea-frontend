package ru.persea.frontend.ui.screens.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.primitives.UnsignedInts.toLong
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.persea.frontend.data.api.products.ProductRetrofitClient
import ru.persea.frontend.data.model.products.*
import ru.persea.frontend.data.model.products.Unit
import kotlin.collections.emptyList
import kotlin.collections.filter
import kotlin.collections.toMutableList

class AdminPanelViewModel : ViewModel() {

    private val _factors = MutableStateFlow<List<Factor>>(emptyList())
    val factors: StateFlow<List<Factor>> = _factors.asStateFlow()

    private val _units = MutableStateFlow<List<Unit>>(emptyList())
    val units: StateFlow<List<Unit>> = _units.asStateFlow()

    private val _types = MutableStateFlow<List<FactorType>>(emptyList())
    val types: StateFlow<List<FactorType>> = _types.asStateFlow()

    private val _categories = MutableStateFlow<List<CategoryDto>>(emptyList())
    val categories: StateFlow<List<CategoryDto>> = _categories.asStateFlow()

    private val _brands = MutableStateFlow<List<BrandDto>>(emptyList())
    val brands: StateFlow<List<BrandDto>> = _brands.asStateFlow()

    private val _products = MutableStateFlow<List<ProductDto>>(emptyList())
    val products: StateFlow<List<ProductDto>> = _products.asStateFlow()

    private val _allFactors = MutableStateFlow<List<Factor>>(emptyList())
    val allFactors: StateFlow<List<Factor>> = _allFactors.asStateFlow()

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
                _categories.value = ProductRetrofitClient.api.getAllCategories()
                _brands.value = ProductRetrofitClient.api.getAllBrands()
                _products.value = ProductRetrofitClient.api.getAllProducts()
                _allFactors.value = ProductRetrofitClient.api.getFactors()
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

    fun createCategory(name: String, code: String) {
        viewModelScope.launch {
            try {
                val newCategory = ProductRetrofitClient.api.createCategory(
                    CategoryDto(id = null, name = name, code = code)
                )
                val currentList = _categories.value.toMutableList()
                currentList.add(newCategory)
                _categories.value = currentList
                _message.value = "Категория создана: ${newCategory.name}"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun updateCategory(id: Long, name: String, code: String) {
        viewModelScope.launch {
            try {
                val updated = ProductRetrofitClient.api.updateCategory(
                    id, CategoryDto(id = id, name = name, code = code)
                )
                val currentList = _categories.value.toMutableList()
                val index = currentList.indexOfFirst { it.id == id }
                if (index != -1) {
                    currentList[index] = updated
                    _categories.value = currentList
                }
                _message.value = "Категория обновлена: ${updated.name}"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            try {
                ProductRetrofitClient.api.deleteCategory(id)
                _categories.value = _categories.value.filter { it.id != id }
                _message.value = "Категория удалена"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun createBrand(name: String, description: String?) {
        viewModelScope.launch {
            try {
                val newBrand = ProductRetrofitClient.api.createBrand(
                    BrandDto(id = null, name = name, description = description)
                )
                val currentList = _brands.value.toMutableList()
                currentList.add(newBrand)
                _brands.value = currentList
                _message.value = "Бренд создан: ${newBrand.name}"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun updateBrand(id: Long, name: String, description: String?) {
        viewModelScope.launch {
            try {
                val updated = ProductRetrofitClient.api.updateBrand(
                    id, BrandDto(id = id, name = name, description = description)
                )
                val currentList = _brands.value.toMutableList()
                val index = currentList.indexOfFirst { it.id == id }
                if (index != -1) {
                    currentList[index] = updated
                    _brands.value = currentList
                }
                _message.value = "Бренд обновлён: ${updated.name}"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun deleteBrand(id: Long) {
        viewModelScope.launch {
            try {
                ProductRetrofitClient.api.deleteBrand(id)
                _brands.value = _brands.value.filter { it.id != id }
                _message.value = "Бренд удалён"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun createProduct(
        name: String,
        categoryId: Long,
        brandId: Long,
        imageURI: String?,
        barcode: String?,
        numericFactors: List<NumericFactorInput>?,
        booleanFactors: List<BooleanFactorInput>?,
        enumFactors: List<EnumFactorInput>?
    ) {
        viewModelScope.launch {
            try {
                val request = CreateProductRequest(
                    name = name,
                    categoryId = categoryId,
                    brandId = brandId,
                    imageURI = imageURI,
                    barcode = barcode,
                    numericFactors = numericFactors ?: emptyList(),
                    booleanFactors = booleanFactors ?: emptyList(),
                    enumFactors = enumFactors ?: emptyList()
                )
                val newProduct = ProductRetrofitClient.api.createProduct(request)
                val newApiProduct = ProductDto(
                    id = newProduct.id,
                    name = newProduct.name,
                    rating = newProduct.rating,
                    imageURI = newProduct.imageURI,
                    factors = null
                )
                _products.value += newApiProduct
                _message.value = "Продукт создан: ${newProduct.name}"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            try {
                ProductRetrofitClient.api.deleteProduct(id)
                _products.value = _products.value.filter { it.id != id }
                _message.value = "Продукт удалён"
            } catch (e: Exception) {
                _message.value = "Ошибка: ${e.message}"
            }
        }
    }
}