package ru.persea.frontend.ui.screens.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.persea.frontend.data.api.users.UserRetrofitClient
import ru.persea.frontend.data.model.users.FavoriteProduct
import ru.persea.frontend.data.model.users.ProductDto

class ProfileViewModel : ViewModel() {

    var viewedProducts by mutableStateOf<List<ProductDto>>(emptyList())
    private set
    var scannedProducts by mutableStateOf<List<ProductDto>>(emptyList())
    private set
    var favorites by mutableStateOf<List<FavoriteProduct>>(emptyList())
    private set
    var isLoading by mutableStateOf(false)
    private set

    fun loadUserData() {
        viewModelScope.launch {
            isLoading = true
            try {
                val viewed = UserRetrofitClient.api.getMyViewedProducts(page = 0, size = 20)
                val scanned = UserRetrofitClient.api.getMyScannedProducts(page = 0, size = 20)
                val favs = UserRetrofitClient.api.getMyFavorites(page = 0, size = 20)

                viewedProducts = viewed
                scannedProducts = scanned
                favorites = favs
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}