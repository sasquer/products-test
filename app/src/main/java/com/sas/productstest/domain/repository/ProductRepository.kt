package com.sas.productstest.domain.repository

import com.sas.productstest.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun fetchAndSaveProducts(): List<Product>
    fun getProducts(): Flow<List<Product>>
    suspend fun getProductById(id: Int): Product?
    suspend fun updateProduct(productId: Int, name: String, description: String)
}
