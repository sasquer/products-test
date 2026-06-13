package com.sas.productstest.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("product_id") val productId: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("price") val price: Double?,
    @SerializedName("unit") val unit: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("discount") val discount: Int?,
    @SerializedName("availability") val availability: Boolean?,
    @SerializedName("brand") val brand: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("reviews") val reviews: List<ReviewDto>?
)

data class ReviewDto(
    @SerializedName("user_id") val userId: Int?,
    @SerializedName("rating") val rating: Int?,
    @SerializedName("comment") val comment: String?
)
