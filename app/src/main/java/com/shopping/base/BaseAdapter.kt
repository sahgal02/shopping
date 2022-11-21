package com.shopping.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.shopping.R
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.entities.ShimmerModel
import com.shopping.ui.adapter.ProductAdapter
import com.shopping.variables.interfaces.Constants

abstract class BaseAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {
    open var modelList: ArrayList<BaseModel> = arrayListOf()
    open var size = 0

    /**
     * Notify Adapter Item Inserted
     */
    open fun notifyAdapterItemInsert(position: Int, model: BaseModel) {
        modelList.add(position, model)
        size += 1
        notifyItemInserted(position)
    }

    /**
     * Notify Adapter Item Inserted
     */
    open fun notifyAdapterItemInsert(model: BaseModel) {
        modelList.add(model)
        size += 1
        notifyItemInserted(size - 1)
    }

    /**
     * Notify adapter to update data set
     * @author Jatin Sahgal
     */
    open fun notifyAdapterDataSetChanged() {
        size = modelList.size
        notifyDataSetChanged()
    }

    /**
     * Notify adapter to update data set
     * @author Jatin Sahgal
     */
    open fun notifyAdapterDataSetChanged(modelList: ArrayList<BaseModel>) {
        val callback = DiffUtil.calculateDiff(
            Callback(
                this.modelList, modelList
            )
        )
        this.modelList.clear()
        this.modelList.addAll(modelList)
        callback.dispatchUpdatesTo(this)
        notifyAdapterDataSetChanged()
    }

    /**
     * Clear adapter item
     */
    open fun clearAdapterItem() {
        modelList.clear()
        notifyAdapterDataSetChanged()
    }

    /**
     * Notify adapter to update data set as per range change
     * @author Jatin Sahgal
     */
    open fun notifyAdapterItemRangeChanged() {
        val lastSize = size
        size = modelList.size
        if (size > lastSize) {
            notifyItemRangeChanged(lastSize, size - lastSize)
        } else
            notifyAdapterDataSetChanged()
    }

    fun addShimmerList(shimmerSize: Int? = Constants.SHIMMER_SIZE) {
        for (index in 0..shimmerSize!!) {
            modelList.add(ShimmerModel())
        }
        if (size == 0) {
            notifyAdapterDataSetChanged()
        } else {
            notifyAdapterItemRangeChanged()
        }
    }

    /**
     * [clearShimmerList] remove shimmer data from base model
     */
    fun clearShimmerList() {
        val shimmerList = arrayListOf<BaseModel>()
        for (model in modelList) {
            if (model is ShimmerModel) {
                shimmerList.add(model)
            }
        }
        modelList.removeAll(shimmerList.toSet())
        notifyAdapterItemRangeChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (modelList[position] is ShimmerModel) VIEW_SHIMMER else super.getItemViewType(
            position
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        return super.createViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return size
    }

    companion object {
        const val VIEW_SHIMMER = 1111
    }

    inner class ShimmerViewHolder(private val binding: ViewBinding) :
        BaseViewHolder<BaseModel>(binding) {

        override fun bindTo(baseModel: BaseModel, position: Int) {
            binding.root.findViewById<ShimmerFrameLayout>(R.id.view_shimmer).startShimmer()
        }
    }


    class Callback(
        private val oldList: List<BaseModel>,
        private val newList: List<BaseModel>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

        override fun areContentsTheSame(oldCourse: Int, newPosition: Int): Boolean {
            return oldList[oldCourse] == newList[newPosition]
        }
    }
}


abstract class BaseViewHolder<in BaseModel>(
    viewBinding: ViewBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    abstract fun bindTo(baseModel: BaseModel, position: Int)
}
