package ru.persea.frontend.ui.screens.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.persea.frontend.data.api.recommendation.RecommendationRetrofitClient
import ru.persea.frontend.data.model.recommendation.RecommendationFeed

class RecommendationViewModel : ViewModel() {

    var feed by mutableStateOf<RecommendationFeed?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun loadRecommendations() {
        viewModelScope.launch {
            isLoading = true
            error = null

            try {
                feed = RecommendationRetrofitClient.api.getRecommendationFeed(limit = 20)
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }
}