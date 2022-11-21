package com.shopping.jetpacks.daos

import androidx.room.*
import com.shopping.jetpacks.entities.ProductModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Transaction
    @Query("SELECT * FROM PRODUCTS")
    fun select(): Flow<List<ProductModel>>

    @Transaction
    @Query("SELECT * FROM PRODUCTS WHERE product_id != :id LIMIT 0,:size")
    fun selectSimilar(id: String, size: Int): Flow<List<ProductModel>>

    @Transaction
    @Query("SELECT * FROM PRODUCTS WHERE is_in_wishlist = 1")
    fun selectBookmarked(): Flow<List<ProductModel>>

    @Transaction
    @Query("SELECT * FROM PRODUCTS WHERE is_in_wishlist = 1")
    fun selectBookmarkedItems(): List<ProductModel>

    @Transaction
    @Query("SELECT COUNT(product_id) FROM PRODUCTS WHERE is_in_wishlist = 1")
    fun selectBookmarkCount(): Flow<Int>

    @Transaction
    @Query("SELECT * FROM PRODUCTS WHERE product_id = :id")
    fun selectById(id: String): ProductModel

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(models: List<ProductModel>?)

    @Transaction
    @Update
    fun update(model: ProductModel)

    @Transaction
    @Query("UPDATE PRODUCTS SET is_in_wishlist=:isInWishlist WHERE product_id = :id")
    fun updateFavorite(id: String, isInWishlist: Boolean)

    @Transaction
    @Query("DELETE FROM PRODUCTS")
    fun clear()
}
