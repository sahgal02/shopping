package com.shopping.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.shopping.R
import com.shopping.base.BaseAdapter
import com.shopping.base.BaseModel
import com.shopping.base.BaseViewHolder
import com.shopping.databinding.AdapterPurchaseItemBinding
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.extensions.setString
import com.shopping.utilities.storyLogs
import com.shopping.variables.enums.GetString

class PurchaseAdapter(
    private val isAddToCartEnable: Boolean,
    private val addToCartButtonText: String?,
    private val listener: (ProductModel.PurchaseModel, Int) -> Unit
) : BaseAdapter<BaseViewHolder<BaseModel>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BaseModel> {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BaseModel>, position: Int) {
        if (holder is ViewHolder)
            holder.bindTo(modelList[position], position)
    }

    inner class ViewHolder(private val binding: AdapterPurchaseItemBinding) :
        BaseViewHolder<BaseModel>(binding), View.OnClickListener {

        constructor(
            parent: ViewGroup
        ) : this(
            AdapterPurchaseItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun bindTo(baseModel: BaseModel, position: Int) {
            if (baseModel is ProductModel.PurchaseModel)
                with(binding) {
                    clickListener = this@ViewHolder
                    title.setString(baseModel.displayName)
                    price.setString(baseModel.unitPrice)
                    description.setString("(Cart Qty : ${baseModel.cartQty} | Min qty : ${baseModel.minQtyLimit} | Max qty : ${baseModel.minQtyLimit})")
                    root.findViewById<TextView>(R.id.button_text)
                        .setString(addToCartButtonText ?: GetString.BUTTON_ADD)
                    if (isAddToCartEnable) {
                        if (baseModel.selectedCount > 0) {
                            binding.root.findViewById<TextView>(R.id.selected_items)
                                .setString(
                                    baseModel.selectedCount.toString()
                                )
                            showVisibility(
                                binding.root.findViewById(R.id.parent_selected),
                                binding.root.findViewById(R.id.button_add)
                            )
                        } else {
                            hideVisibility(
                                binding.root.findViewById(R.id.parent_selected),
                                binding.root.findViewById(R.id.button_add)
                            )
                        }
                    } else {
                        binding.root.findViewById<View>(R.id.parent_selected).isVisible = false
                        binding.root.findViewById<View>(R.id.button_add).isVisible = false
                    }
                }
        }

        override fun onClick(v: View?) {
            val model = modelList[absoluteAdapterPosition] as ProductModel.PurchaseModel
            when (v!!.id) {
                R.id.button_add, R.id.button_add_more -> {
                    model.selectedCount += 1
                    binding.root.findViewById<TextView>(R.id.selected_items).text =
                        model.selectedCount.toString()
                    showVisibility(
                        binding.root.findViewById(R.id.parent_selected),
                        binding.root.findViewById(R.id.button_add)
                    )
                    listener(model, ACTION_ADD)
                }
                R.id.button_sub -> {
                    if (model.selectedCount > 1)
                        model.selectedCount -= 1
                    else
                        model.selectedCount = 0
                    if (model.selectedCount > 0) {
                        binding.root.findViewById<TextView>(R.id.selected_items).text =
                            model.selectedCount.toString()
                    } else {
                        hideVisibility(
                            binding.root.findViewById(R.id.parent_selected),
                            binding.root.findViewById(R.id.button_add)
                        )
                    }
                    listener(model, ACTION_SUBTRACT)
                }
            }
        }
    }

    companion object {
        const val ACTION_BOOKMARK = 4
        const val ACTION_PREVIEW = 3
        const val ACTION_EXPLORE = 2
        const val ACTION_SUBTRACT = 1
        const val ACTION_ADD = 0

        fun hideVisibility(selectedView: View, buttonAdd: View) {
            selectedView.isVisible = false
            buttonAdd.isVisible = true
        }

        fun showVisibility(selectedView: View, buttonAdd: View) {
            selectedView.isVisible = true
            buttonAdd.isVisible = false
        }

    }
}
