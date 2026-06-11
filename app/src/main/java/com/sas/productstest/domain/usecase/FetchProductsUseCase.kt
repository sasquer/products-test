package com.sas.productstest.domain.usecase

import com.sas.productstest.domain.model.Product
import com.sas.productstest.domain.repository.ProductRepository
import javax.inject.Inject

class FetchProductsUseCase @Inject constructor(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(): List<Product> = repository.fetchAndSaveProducts()
}
