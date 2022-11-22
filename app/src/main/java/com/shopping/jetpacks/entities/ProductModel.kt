package com.shopping.jetpacks.entities

import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.shopping.base.BaseModel
import com.shopping.jetpacks.converters.MessageModelConverter
import com.shopping.jetpacks.converters.PriceModelConverter
import com.shopping.jetpacks.converters.PurchaseModelConverter
import com.shopping.jetpacks.converters.StringListConverter
import kotlinx.parcelize.Parcelize

data class ProductResponse(
    @SerializedName("products")
    val products: List<ProductModel>? = emptyList()
)

@Parcelize
@Entity(tableName = "products")
data class ProductModel(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @ColumnInfo(name = "product_id")
    val id: String = "",

    @SerializedName("citrusId")
    @ColumnInfo(name = "citrus_id")
    val citrusId: String? = "",

    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String? = "",

    @SerializedName("brand")
    @ColumnInfo(name = "brand")
    val brand: String? = "",

    @SerializedName("imageURL")
    @ColumnInfo(name = "image_url")
    val imageURL: String? = "",

    @TypeConverters(PriceModelConverter::class)
    @ColumnInfo(name = "price")
    @SerializedName("price")
    val price: List<PriceModel>? = emptyList(),

    @TypeConverters(StringListConverter::class)
    @ColumnInfo(name = "badges")
    @SerializedName("badges")
    val badges: List<String>? = emptyList(),

    @ColumnInfo(name = "rating_count")
    @SerializedName("ratingCount")
    val ratingCount: Double? = 0.toDouble(),

    @ColumnInfo(name = "is_add_to_cart_enable")
    @SerializedName("isAddToCartEnable")
    val isAddToCartEnable: Boolean? = false,

    @ColumnInfo(name = "add_to_cart_button_text")
    @SerializedName("addToCartButtonText")
    val addToCartButtonText: String? = "",

    @ColumnInfo(name = "is_in_trolley")
    @SerializedName("isInTrolley")
    val isInTrolley: Boolean? = false,

    @ColumnInfo(name = "is_in_wishlist")
    @SerializedName("isInWishlist")
    var bookmark: Boolean? = false,

    @TypeConverters(PurchaseModelConverter::class)
    @ColumnInfo(name = "purchase_types")
    @SerializedName("purchaseTypes")
    val purchaseTypes: List<PurchaseModel>? = emptyList(),

    @ColumnInfo(name = "is_find_me_enable")
    @SerializedName("isFindMeEnable")
    val isFindMeEnable: Boolean? = false,

    @ColumnInfo(name = "sale_unit_price")
    @SerializedName("saleUnitPrice")
    val saleUnitPrice: Double? = 0.toDouble(),

    @ColumnInfo(name = "total_review_count")
    @SerializedName("totalReviewCount")
    val totalReviewCount: Int? = 0,

    @ColumnInfo(name = "is_delivery_only")
    @SerializedName("isDeliveryOnly")
    val isDeliveryOnly: Boolean? = false,

    @ColumnInfo(name = "is_direct_from_supplier")
    @SerializedName("isDirectFromSupplier")
    val isDirectFromSupplier: Boolean? = false,

    @TypeConverters(MessageModelConverter::class)
    @ColumnInfo(name = "messages")
    @SerializedName("messages")
    val messages: MessageModel? = MessageModel(),

    @ColumnInfo(name = "selected_count")
    @SerializedName("selectedCount")
    var selectedCount: Int = 0
) : BaseModel(), Parcelable {

    @Parcelize
    data class PriceModel(
        @SerializedName("message")
        val message: String? = "",
        @SerializedName("value")
        val value: Double? = 0.toDouble(),
        @SerializedName("isOfferPrice")
        val isOfferPrice: Boolean? = false,

        ) : BaseModel(), Parcelable


    @Parcelize
    data class PurchaseModel(
        @SerializedName("purchaseType")
        val purchaseType: String? = "",
        @SerializedName("displayName")
        val displayName: String? = "",
        @SerializedName("unitPrice")
        val unitPrice: Double? = 0.toDouble(),
        @SerializedName("isOfferPrice")
        val isOfferPrice: Boolean? = false,

        @SerializedName("minQtyLimit")
        val minQtyLimit: Int? = 0,

        @SerializedName("maxQtyLimit")
        val maxQtyLimit: Int? = 0,

        @SerializedName("cartQty")
        val cartQty: Int? = 0,

        /**
         * For cart
         */
        @SerializedName("selectedCount")
        var selectedCount: Int = 0

    ) : BaseModel()

    @Parcelize
    data class MessageModel(
        @SerializedName("secondaryMessage")
        val secondaryMessage: String? = "",
    ) : BaseModel(), Parcelable
}

data class ProductRequest(
    val id: String? = "",
    val size: Int = 0,
    val type: Type = Type.SELECT
) {
    enum class Type {
        BOOKMARK,
        SELECT,
        SELECT_EXCEPT,
        SELECT_BY_ID
    }
}

data class ProductBookmarkRequest(
    val id: String? = "",
    val isInWishlist: Boolean? = false
)