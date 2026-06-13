package com.sas.productstest.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sas.productstest.data.local.entity.ProductEntity
import com.sas.productstest.data.local.entity.ReviewEntity
import com.sas.productstest.data.remote.dto.ProductDto
import com.sas.productstest.data.remote.dto.ReviewDto
import com.sas.productstest.domain.model.Product
import com.sas.productstest.domain.model.Review

private val gson = Gson()

fun ProductDto.toEntity(): ProductEntity = ProductEntity(
    productId = productId,
    name = name ?: "",
    description = description ?: "",
    price = price ?: 0.0,
    unit = unit ?: "Piece",
    image = image ?: "",
    discount = discount ?: 0,
    availability = availability ?: false,
    brand = brand ?: "",
    category = category ?: "",
    rating = rating ?: 0.0,
    reviews = gson.toJson((reviews ?: emptyList()).map { it.toReviewEntity() })
)

fun ReviewDto.toReviewEntity(): ReviewEntity = ReviewEntity(
    userId = userId ?: 0,
    rating = rating ?: 0,
    comment = comment ?: ""
)

fun ProductDto.toDomain(): Product = Product(
    productId = productId,
    name = name ?: "",
    description = description ?: "",
    price = price ?: 0.0,
    unit = unit ?: "Piece",
    image = image ?: "",
    discount = discount ?: 0,
    availability = availability ?: false,
    brand = brand ?: "",
    category = category ?: "",
    rating = rating ?: 0.0,
    reviews = (reviews ?: emptyList()).map { it.toDomain() }
)

fun ReviewDto.toDomain(): Review = Review(
    userId = userId ?: 0,
    rating = rating ?: 0,
    comment = comment ?: ""
)

fun ProductEntity.toDomain(): Product {
    val type = object : TypeToken<List<ReviewEntity>>() {}.type
    val reviewEntities: List<ReviewEntity> = gson.fromJson(reviews, type) ?: emptyList()
    return Product(
        productId = productId,
        name = name,
        description = description,
        price = price,
        unit = unit,
        image = image,
        discount = discount,
        availability = availability,
        brand = brand,
        category = category,
        rating = rating,
        reviews = reviewEntities.map { it.toDomain() }
    )
}

fun ReviewEntity.toDomain(): Review = Review(
    userId = userId,
    rating = rating,
    comment = comment
)
