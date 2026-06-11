package com.sas.productstest.data.local.entity.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sas.productstest.data.local.entity.ReviewEntity

class ReviewListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromReviewList(reviews: List<ReviewEntity>): String =
        gson.toJson(reviews)

    @TypeConverter
    fun toReviewList(json: String): List<ReviewEntity> {
        val type = object : TypeToken<List<ReviewEntity>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}
