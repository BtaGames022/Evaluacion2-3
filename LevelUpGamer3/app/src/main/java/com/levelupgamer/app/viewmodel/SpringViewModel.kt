package com.levelupgamer.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levelupgamer.app.data.SpringRepository
import com.levelupgamer.app.model.SpringProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SpringUiState(
    val products: List<SpringProduct> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class SpringViewModel(
    private val repository: SpringRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpringUiState())
    val uiState: StateFlow<SpringUiState> = _uiState.asStateFlow()

    init {
        fetchBackendProducts()
    }

    fun fetchBackendProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val items = repository.getProductsFromBackend()
                _uiState.update { it.copy(isLoading = false, products = items) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Error: ${e.message}")
                }
            }
        }
    }
}