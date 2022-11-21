package com.shopping.jetpacks.daos

import androidx.room.*
import com.shopping.jetpacks.entities.CartModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Transaction
    @Query("SELECT COUNT(cart_id) FROM CART")
    fun selectCount(): Flow<Int>

    @Transaction
    @Query("SELECT * FROM CART")
    fun select(): List<CartModel>

    @Transaction
    @Query("SELECT * FROM CART WHERE cart_id LIKE :id || '%'")
    fun selectById(id: String): List<CartModel>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(models: CartModel)

    @Delete

    fun remove(model: CartModel)

    @Transaction
    @Query("DELETE FROM CART")
    fun clear()
}
