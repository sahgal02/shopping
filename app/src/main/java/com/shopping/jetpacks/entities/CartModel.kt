package com.shopping.jetpacks.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shopping.base.BaseModel

@Entity(tableName = "cart", foreignKeys = [])
data class CartModel(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "cart_id")
    val cartId: String = "",

    @ColumnInfo(name = "items")
    val itemCount: Int = 0,

    ) : BaseModel()