package com.sas.productstest.domain.usecase

import com.sas.productstest.domain.model.Product
import com.sas.productstest.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository,
) {
    operator fun invoke(): Flow<List<Product>> = repository.getProducts()
}
