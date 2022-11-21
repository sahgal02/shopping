package com.shopping.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.shopping.R
import com.shopping.base.BaseAdapter
import com.shopping.base.BaseModel
import com.shopping.base.BaseViewHolder
import com.shopping.databinding.AdapterItemShimmerBinding
import com.shopping.databinding.AdapterItemShimmerHorizontalBinding
import com.shopping.databinding.AdapterProductHorizontalBinding
import com.shopping.databinding.AdapterProductVerticleBinding
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.extensions.loadImage
import com.shopping.jetpacks.extensions.setString
import com.shopping.ui.adapter.PurchaseAdapter.Companion.hideVisibility
import com.shopping.ui.adapter.PurchaseAdapter.Companion.showVisibility
import com.shopping.ui.frag.ProductListFrag
import com.shopping.utilities.storyLogs
import com.shopping.variables.enums.GetString

class ProductAdapter(
    private val type: Int = ProductListFrag.TYPE_LIST,
    private val listener: (ProductModel, Int, Int) -> Unit
) : BaseAdapter<BaseViewHolder<BaseModel>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BaseModel> {
        return when (viewType) {
            VIEW_SHIMMER -> ShimmerViewHolder(
                if (type == ProductListFrag.TYPE_OTHER) {
                    AdapterItemShimmerHorizontalBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                } else
                    AdapterItemShimmerBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
            )
            else -> if (type == ProductListFrag.TYPE_OTHER)
                ViewHolderHorizontal(parent)
            else ViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BaseModel>, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bindTo(modelList[position], position)
            }
            is ViewHolderHorizontal -> {
                holder.bindTo(modelList[position], position)
            }
        }
    }

    inner class ViewHolder(private val binding: AdapterProductVerticleBinding) :
        BaseViewHolder<BaseModel>(binding), View.OnClickListener {

        constructor(
            parent: ViewGroup
        ) : this(
            AdapterProductVerticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun bindTo(baseModel: BaseModel, position: Int) {
            if (baseModel is ProductModel) {
                with(binding) {
                    clickListener = this@ViewHolder
                    bindData(
                        binding.title, binding.description, binding.image,
                        binding.price,
                        root.findViewById(R.id.button_text),
                        binding.root.findViewById(R.id.selected_items),
                        binding.root,
                        baseModel
                    )
                    initializeBookmark(bookmark, baseModel)
                }
                binding.root.findViewById<View>(R.id.divider).isVisible = position != size - 1
            }
        }


        override fun onClick(v: View?) {
            commonClick(
                v, (modelList[absoluteAdapterPosition] as ProductModel),
                absoluteAdapterPosition, binding.bookmark, binding.root
            )
        }
    }

    inner class ViewHolderHorizontal(private val binding: AdapterProductHorizontalBinding) :
        BaseViewHolder<BaseModel>(binding), View.OnClickListener {

        constructor(
            parent: ViewGroup
        ) : this(
            AdapterProductHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun bindTo(baseModel: BaseModel, position: Int) {
            if (baseModel is ProductModel) {
                with(binding) {
                    this.position = position
                    this.size = this@ProductAdapter.size
                    this.clickListener = this@ViewHolderHorizontal
                    bindData(
                        binding.title, null, binding.image,
                        binding.price,
                        root.findViewById(R.id.button_text),
                        binding.root.findViewById(R.id.selected_items),
                        binding.root,
                        baseModel
                    )
                    initializeBookmark(bookmark, baseModel)
                }
            }
        }


        override fun onClick(v: View?) {
            commonClick(
                v, (modelList[absoluteAdapterPosition] as ProductModel),
                absoluteAdapterPosition, binding.bookmark, binding.root
            )
        }
    }

    private fun commonClick(
        v: View?, productModel: ProductModel, absoluteAdapterPosition: Int,
        bookmark: ImageView,
        root: View
    ) {
        when (v!!.id) {
            R.id.bookmark -> {
                productModel.bookmark = !productModel.bookmark!!
                initializeBookmark(bookmark, productModel)
                listener(
                    productModel,
                    PurchaseAdapter.ACTION_BOOKMARK,
                    absoluteAdapterPosition
                )
            }
            R.id.title, R.id.description, R.id.image -> {
                listener(
                    productModel,
                    PurchaseAdapter.ACTION_PREVIEW,
                    absoluteAdapterPosition
                )
            }
            else -> {
                if (!productModel.purchaseTypes.isNullOrEmpty() && productModel.purchaseTypes.size == 1) {
                    val model = productModel.purchaseTypes[0]
                    when (v.id) {
                        R.id.button_add, R.id.button_add_more -> {
                            model.selectedCount += 1
                            productModel.selectedCount += 1
                            root.findViewById<TextView>(R.id.selected_items)
                                .setString(productModel.selectedCount.toString())
                            showVisibility(
                                root.findViewById(R.id.parent_selected),
                                root.findViewById(R.id.button_add)
                            )
                            listener(
                                productModel,
                                PurchaseAdapter.ACTION_ADD,
                                absoluteAdapterPosition
                            )
                        }
                        R.id.button_sub -> {
                            model.selectedCount -= 1
                            productModel.selectedCount -= 1
                            if (productModel.selectedCount > 0) {
                                root.findViewById<TextView>(R.id.selected_items)
                                    .setString(productModel.selectedCount.toString())
                            } else {
                                hideVisibility(
                                    root.findViewById(R.id.parent_selected),
                                    root.findViewById(R.id.button_add)
                                )
                            }
                            listener(
                                productModel,
                                PurchaseAdapter.ACTION_SUBTRACT,
                                absoluteAdapterPosition
                            )
                        }
                    }
                } else {
                    listener(
                        productModel,
                        PurchaseAdapter.ACTION_EXPLORE,
                        absoluteAdapterPosition
                    )
                }
            }
        }
    }

    private fun bindData(
        title: TextView,
        description: TextView?,
        imageView: ImageView,
        price: TextView,
        buttonText: TextView,
        selectedItems: TextView,
        rootView: View,
        baseModel: ProductModel
    ) {
        title.setString(baseModel.title)
        description?.setString(baseModel.brand)
        imageView.loadImage(baseModel.imageURL ?: "", R.drawable.placeholder_one)
        price.setString(baseModel.saleUnitPrice)
        buttonText.setString(GetString.BUTTON_ADD)
        if (baseModel.selectedCount > 0) {
            selectedItems.setString(
                baseModel.selectedCount.toString()
            )
            showVisibility(
                rootView.findViewById(R.id.parent_selected),
                rootView.findViewById(R.id.button_add)
            )
        } else {
            hideVisibility(
                rootView.findViewById(R.id.parent_selected),
                rootView.findViewById(R.id.button_add)
            )
        }
    }


    private fun initializeBookmark(bookmark: ImageView, model: ProductModel) {
        bookmark.apply {
            setColorFilter(
                ContextCompat.getColor(
                    bookmark.context,
                    if (model.bookmark!!)
                        R.color.colorBookmark
                    else
                        R.color.colorIconLight
                )
            )
            setImageResource(
                if (model.bookmark!!)
                    R.drawable.icon_vd_fav_filled
                else
                    R.drawable.icon_vd_fav
            )
        }

    }
}