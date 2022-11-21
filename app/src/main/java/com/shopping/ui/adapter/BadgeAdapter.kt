package com.shopping.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shopping.R
import com.shopping.databinding.AdapterBadgeBinding
import com.shopping.jetpacks.extensions.loadImage
import com.shopping.utilities.storyLogs

class BadgeAdapter(private val modelList: List<String>) :
    RecyclerView.Adapter<BadgeAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        storyLogs(modelList.size)
        return modelList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeAdapter.ViewHolder {
        return ViewHolder(parent)
    }


    override fun onBindViewHolder(holder: BadgeAdapter.ViewHolder, position: Int) {
        holder.bindTo(modelList[position])
    }

    inner class ViewHolder(private val binding: AdapterBadgeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        constructor(
            parent: ViewGroup
        ) : this(
            AdapterBadgeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        fun bindTo(baseModel: String) {
            storyLogs(baseModel)
            with(binding) {
                image.loadImage(baseModel, R.drawable.circular_shimmer)
            }
        }
    }


}