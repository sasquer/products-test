package com.sas.productstest.presentation.fetch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sas.productstest.domain.model.Product
import com.sas.productstest.domain.usecase.FetchProductsUseCase
import com.sas.productstest.domain.usecase.GetProductsUseCase
import com.sas.productstest.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FetchViewModel @Inject constructor(
    private val fetchProductsUseCase: FetchProductsUseCase,
    getProductsUseCase: GetProductsUseCase,
) : ViewModel() {

    private val _fetchState = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val fetchState: StateFlow<UiState<List<Product>>> = _fetchState.asStateFlow()

    val hasCachedData: StateFlow<Boolean> = getProductsUseCase()
        .map { it.isNotEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    private var fetchDataJob: Job? = null

    fun fetchProducts() {
        if (_fetchState.value is UiState.Loading) return

        fetchDataJob?.cancel()
        fetchDataJob = viewModelScope.launch {
            _fetchState.value = UiState.Loading

            try {
                val products = fetchProductsUseCase()
                _fetchState.value = UiState.Success(products)
            } catch (ex: Exception) {
                if (_fetchState.value is UiState.Loading) {
                    _fetchState.value = UiState.Error(ex.message ?: "Невідома помилка")
                }
            }
        }
    }

    fun resetState() {
        _fetchState.value = UiState.Idle
    }

}
