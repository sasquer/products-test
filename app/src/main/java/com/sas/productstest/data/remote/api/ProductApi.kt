package com.sas.productstest.data.remote.api

import com.sas.productstest.data.remote.dto.ProductDto
import retrofit2.http.GET

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>
}
