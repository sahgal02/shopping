package com.shopping.ui.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.shopping.R
import com.shopping.base.BaseSheetDialogFragment
import com.shopping.base.observe
import com.shopping.databinding.SheetItemBinding
import com.shopping.jetpacks.entities.CartModel
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.extensions.setString
import com.shopping.jetpacks.viewmodels.CartVM
import com.shopping.ui.adapter.PurchaseAdapter
import com.shopping.utilities.MyApplication

class ItemsSheet : BaseSheetDialogFragment(), View.OnClickListener {
    private lateinit var binding: SheetItemBinding
    private val viewModel by viewModels<CartVM>()
    private var listener: (ProductModel) -> Unit = {}
    private lateinit var model: ProductModel
    private lateinit var adapter: PurchaseAdapter

    fun showDialog(
        fragmentManager: FragmentManager,
        model: ProductModel,
        listener: (ProductModel) -> Unit
    ) {
        this.model = model
        this.listener = listener
        show(fragmentManager, "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = SheetItemBinding.inflate(
                LayoutInflater.from(requireContext()),
                container, false
            )
        }
        binding.clickListener = this@ItemsSheet
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        initializeView()
        setUpObserver()
        initializeViewModel()
    }

    override fun setUpObserver() {
        super.setUpObserver()
        adapter.modelList.addAll(model.purchaseTypes ?: emptyList())
        adapter.notifyAdapterDataSetChanged()
        observe(viewModel.itemLiveData) {
            model.purchaseTypes?.forEach { purchaseModel ->
                for (cartModel in it) {
                    if (cartModel.cartId == model.id + "_" + purchaseModel.unitPrice) {
                        purchaseModel.selectedCount = cartModel.itemCount
                        break
                    }
                }
            }
            adapter.modelList.clear()
            adapter.modelList.addAll(model.purchaseTypes ?: emptyList())
            adapter.notifyAdapterDataSetChanged()
        }
    }

    override fun initializeViewModel() {
        super.initializeViewModel()
        viewModel.fetchById(model)
    }

    private fun calculateSum() {
        with(binding) {
            parentAction.isVisible = MyApplication.itemCount != 0
            parentAmount.isVisible = MyApplication.itemCount != 0
            selectedItems.setString("${MyApplication.itemCount} Items selected")
            totalAmount.setString(String.format("%.1f", MyApplication.cartTotal))
        }
    }

    override fun initializeView() {
        super.initializeView()
        binding.apply {
            idTextTitle.setString(model.title)
            idTextCategory.setString(model.brand)
        }
        if (MyApplication.itemCount != 0) {
            calculateSum()
        }
    }

    override fun initializeRecyclerView() {
        super.initializeRecyclerView()
        adapter = PurchaseAdapter(
            model.isAddToCartEnable ?: true,
            model.addToCartButtonText
        ) { purchaseModel, action ->
            if (action == PurchaseAdapter.ACTION_ADD) {
                model.selectedCount += 1
                MyApplication.itemCount += 1
                MyApplication.cartTotal += purchaseModel.unitPrice ?: 0.toDouble()
            } else {
                MyApplication.itemCount -= 1
                model.selectedCount -= 1
                MyApplication.cartTotal -= purchaseModel.unitPrice ?: 0.toDouble()
            }
            calculateSum()
            viewModel.updateOrAdd(
                CartModel(
                    "${model.id}_${purchaseModel.unitPrice ?: 0.toDouble()}",
                    purchaseModel.selectedCount
                )
            )
            viewModel.updateProduct(model)
        }
        binding.idRecyclerView.apply {
            this.adapter = this@ItemsSheet.adapter
            isNestedScrollingEnabled = false
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.button_add) {

        } else {
            listener(model)
            dismiss()
        }
    }
}