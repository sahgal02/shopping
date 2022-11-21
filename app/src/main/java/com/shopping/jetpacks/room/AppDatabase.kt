package com.shopping.jetpacks.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shopping.jetpacks.converters.MessageModelConverter
import com.shopping.jetpacks.converters.PriceModelConverter
import com.shopping.jetpacks.converters.PurchaseModelConverter
import com.shopping.jetpacks.converters.StringListConverter
import com.shopping.jetpacks.daos.CartDao
import com.shopping.jetpacks.daos.ProductDao
import com.shopping.jetpacks.entities.CartModel
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.variables.interfaces.DatabaseKeys

/**
 * AppDatabase Class and Table Structure
 *
 * @added : [ProductModel], ,
 */
@Database(
    entities = [
        ProductModel::class,
        CartModel::class],
    version = DatabaseKeys.DATABASE_VERSION, exportSchema = false
)
@TypeConverters(
    MessageModelConverter::class,
    PurchaseModelConverter::class,
    PriceModelConverter::class,
    StringListConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    /**
     * [androidx.room.Dao] representation
     */
    abstract fun productDao(): ProductDao

    abstract fun cartDao(): CartDao

    companion object {
        /**
         * Getting object
         */
        fun get(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java, DatabaseKeys.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}