package com.sas.productstest.presentation.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sas.productstest.domain.model.Product
import com.sas.productstest.domain.usecase.GetProductByIdUseCase
import com.sas.productstest.domain.usecase.UpdateProductUseCase
import com.sas.productstest.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val productId: Int = checkNotNull(savedStateHandle["productId"])

    private val _product = MutableStateFlow<UiState<Product>>(UiState.Loading)
    val product: StateFlow<UiState<Product>> = _product.asStateFlow()

    private val _saveResult = MutableSharedFlow<Boolean>()
    val saveResult: SharedFlow<Boolean> = _saveResult.asSharedFlow()

    init {
        loadProduct()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            val selectedProduct = getProductByIdUseCase(productId)
            _product.value = if (selectedProduct != null) UiState.Success(selectedProduct)
            else UiState.Error("Продукт не знайдено")
        }
    }

    fun saveProduct(name: String, description: String) {
        if (name.isBlank() || description.isBlank()) return
        viewModelScope.launch {
            updateProductUseCase(productId, name.trim(), description.trim())
            _saveResult.emit(true)
        }
    }

}