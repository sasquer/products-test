package com.sas.productstest.domain.usecase

import com.sas.productstest.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Int, name: String, description: String) {
        repository.updateProduct(productId, name, description)
    }
}
