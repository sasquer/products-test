package com.sas.productstest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.sas.productstest.data.local.entity.converters.ReviewListConverter

@Entity(tableName = "products")
@TypeConverters(ReviewListConverter::class)
data class ProductEntity(
    @PrimaryKey val productId: Int,
    val name: String,
    val description: String,
    val price: Double,
    val unit: String,
    val image: String,
    val discount: Int,
    val availability: Boolean,
    val brand: String,
    val category: String,
    val rating: Double,
    val reviews: String // JSON string
)

data class ReviewEntity(
    val userId: Int,
    val rating: Int,
    val comment: String
)
