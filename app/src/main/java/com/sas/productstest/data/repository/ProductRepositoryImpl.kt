package com.sas.productstest.data.repository

import com.sas.productstest.data.local.dao.ProductDao
import com.sas.productstest.data.remote.api.ProductApi
import com.sas.productstest.data.toDomain
import com.sas.productstest.data.toEntity
import com.sas.productstest.domain.model.Product
import com.sas.productstest.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi,
    private val dao: ProductDao,
) : ProductRepository {

    override suspend fun fetchAndSaveProducts(): List<Product> {
        val dtoProducts = api.getProducts()
        val entities = dtoProducts.map { it.toEntity() }
        dao.deleteAll()
        dao.insertProducts(entities)
        return dtoProducts.map { it.toDomain() }
    }

    override fun getProducts(): Flow<List<Product>> =
        dao.getAllProducts().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getProductById(id: Int): Product? = dao.getProductById(id)?.toDomain()

    override suspend fun updateProduct(productId: Int, name: String, description: String) {
        dao.updateProduct(productId, name, description)
    }
}
