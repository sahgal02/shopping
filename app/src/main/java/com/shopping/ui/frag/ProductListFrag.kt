package com.shopping.ui.frag

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shopping.R
import com.shopping.base.BaseFragment
import com.shopping.base.BaseModel
import com.shopping.base.observe
import com.shopping.databinding.FragProductsBinding
import com.shopping.jetpacks.entities.ProductBookmarkRequest
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.entities.ProductRequest
import com.shopping.jetpacks.extensions.isApiFailed
import com.shopping.jetpacks.extensions.isApiSuccessful
import com.shopping.jetpacks.extensions.setString
import com.shopping.jetpacks.viewmodels.ProductVM
import com.shopping.ui.activity.PreviewActivity
import com.shopping.ui.adapter.ProductAdapter
import com.shopping.ui.adapter.PurchaseAdapter
import com.shopping.ui.sheet.ItemsSheet
import com.shopping.utilities.storyLogs
import com.shopping.variables.enums.GetString
import com.shopping.variables.interfaces.IntentKeys

/**
 * Frag implementation for jokes list view
 */
class ProductListFrag : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragProductsBinding
    private val viewModel by viewModels<ProductVM>()
    private lateinit var adapter: ProductAdapter

    /**
     * For home implementation
     */
    private var listener: (Int, Boolean) -> Unit = { _, _ -> }

    /**
     * Changing list view based on [moduleType] : [TYPE_BOOKMARK], [TYPE_LIST], [TYPE_OTHER]
     */
    private val moduleType by lazy {
        requireArguments().getInt(MODULE_TYPE, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragProductsBinding.inflate(
                LayoutInflater.from(requireContext()),
                container, false
            )
        }
        binding.clickListener = this@ProductListFrag
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        setUpObserver()
        initializeRecyclerView()
        initializeViewModel()
    }

    override fun initializeView() {
        super.initializeView()
        binding.divider.isVisible = moduleType != TYPE_OTHER
//        binding.extraSpace.isVisible = moduleType != TYPE_OTHER
    }

    override fun setUpObserver() {
        super.setUpObserver()
        observe(viewModel.productLiveData) {
            adapter.clearShimmerList()
            notifyChange(it)
        }
        when (moduleType) {
            TYPE_LIST -> {
                observe(viewModel.fetchLiveData) {
                    adapter.clearShimmerList()
                    if (isApiSuccessful(it)) {
                        initializeEmptyView(false)
                    } else if (isApiFailed(it)) {
                        initializeEmptyView(true)
                    }
                }
            }
        }
    }

    private fun notifyChange(modelList: List<ProductModel>) {
        val list = arrayListOf<BaseModel>()
        list.addAll(modelList)
        adapter.notifyAdapterDataSetChanged(list)
        initializeEmptyView(modelList.isEmpty())
    }

    override fun initializeEmptyView(isEmpty: Boolean) {
        super.initializeEmptyView(isEmpty)
        when (moduleType) {
            TYPE_BOOKMARK -> {
                binding.parentEmpty.isVisible = isEmpty
                binding.items.isVisible = !isEmpty
                binding.buttonEmpty.isVisible = true
                binding.buttonEmpty.setString(GetString.EMPTY_BUTTON_BOOKMARK)
                binding.inboxDesc.setString(GetString.EMPTY_LABEL_BOOKMARK_DESC)
                binding.inboxTitle.setString(GetString.EMPTY_LABEL_BOOKMARK_TITLE)
            }
            TYPE_LIST -> {
                binding.parentEmpty.isVisible = isEmpty
                binding.items.isVisible = !isEmpty
                binding.buttonEmpty.isVisible = false
                binding.buttonEmpty.setString(GetString.EMPTY_BUTTON_PRODUCT)
                binding.inboxDesc.setString(GetString.EMPTY_LABEL_PRODUCT_DESC)
                binding.inboxTitle.setString(GetString.EMPTY_LABEL_PRODUCT_TITLE)
            }
        }
    }

    override fun initializeViewModel() {
        super.initializeViewModel()
        when (moduleType) {
            TYPE_BOOKMARK -> {
                viewModel.getProduct(
                    ProductRequest(
                        type = ProductRequest.Type.BOOKMARK
                    )
                )
            }
            TYPE_LIST -> {
                adapter.addShimmerList()
                viewModel.fetchProducts()
                viewModel.getProduct(
                    ProductRequest(
                        type = ProductRequest.Type.SELECT
                    )
                )
            }
            TYPE_OTHER -> {
                adapter.addShimmerList()
                viewModel.getProduct(
                    ProductRequest(
                        id = requireArguments().getString(MODULE_ID) ?: "",
                        size = 10,
                        type = ProductRequest.Type.SELECT_EXCEPT
                    )
                )
            }
        }
    }

    override fun initializeRecyclerView() {
        super.initializeRecyclerView()
        adapter = ProductAdapter(type = moduleType) { model, action, adapterPosition ->
            when (action) {
                PurchaseAdapter.ACTION_BOOKMARK -> {
                    listener(PurchaseAdapter.ACTION_BOOKMARK, model.bookmark!!)
                    viewModel.updateBookmark(
                        ProductBookmarkRequest(
                            model.id, model.bookmark
                        )
                    )
                }
                PurchaseAdapter.ACTION_PREVIEW -> {
                    launcher(REQUEST_CODE).launch(
                        Intent(requireContext(), PreviewActivity::class.java)
                            .also {
                                it.putExtra(IntentKeys.INTENT_FOR_MODEL, model)
                            }
                    )

                }
                PurchaseAdapter.ACTION_EXPLORE -> ItemsSheet().showDialog(
                    childFragmentManager,
                    model
                ) { _ ->

                }
            }
        }
        binding.items.apply {
            layoutManager = if (moduleType == TYPE_OTHER)
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            else LinearLayoutManager(requireContext())
            this.adapter = this@ProductListFrag.adapter
            if (moduleType == TYPE_OTHER) {
                isNestedScrollingEnabled = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.clearAdapterItem()
    }

    fun bindListener(listener: (Int, Boolean) -> Unit) {
        this.listener = listener
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.button_empty) {
            listener(0, false)
        }
    }

    override fun onResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onResult(requestCode, resultCode, data)
    }

    companion object {
        private const val REQUEST_CODE = 100
        private const val MODULE_TYPE = "module_type"
        private const val MODULE_ID = "idd"
        const val TYPE_LIST = 0
        const val TYPE_BOOKMARK = 1
        const val TYPE_OTHER = 2

        fun getInstance(moduleType: Int, idIgnore: String? = null): ProductListFrag {
            val frag = ProductListFrag()
            frag.arguments = bundleOf().also {
                it.putInt(MODULE_TYPE, moduleType)
                it.putString(MODULE_ID, idIgnore)
            }
            return frag
        }
    }


}

