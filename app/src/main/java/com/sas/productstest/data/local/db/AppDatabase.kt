package com.sas.productstest.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sas.productstest.data.local.dao.ProductDao
import com.sas.productstest.data.local.entity.ProductEntity
import com.sas.productstest.data.local.entity.converters.ReviewListConverter

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ReviewListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
