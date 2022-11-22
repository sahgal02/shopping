package com.shopping.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shopping.R
import com.shopping.base.BaseActivity
import com.shopping.base.observe
import com.shopping.databinding.ActivityPreviewBinding
import com.shopping.jetpacks.entities.ProductBookmarkRequest
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.extensions.loadImage
import com.shopping.jetpacks.extensions.setString
import com.shopping.jetpacks.viewmodels.ProductVM
import com.shopping.ui.adapter.BadgeAdapter
import com.shopping.ui.frag.ProductListFrag
import com.shopping.ui.sheet.ItemsSheet
import com.shopping.utilities.MyApplication
import com.shopping.utilities.storyLogs
import com.shopping.variables.enums.GetString
import com.shopping.variables.interfaces.IntentKeys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreviewActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityPreviewBinding
    private val viewModel by viewModels<ProductVM>()
    private lateinit var model: ProductModel
    private var shimmerActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.clickListener = this@PreviewActivity
        setToolbar()
        binding.viewShimmer.isVisible = true
        binding.viewShimmer.showShimmer(true)
        if (hasConnection()) {
            init()
        } else {
            ConnectionActivity.launch(this)
        }
    }

    private fun init() {
        lifecycleScope.launch {
            intent.getParcelableExtra<ProductModel>(IntentKeys.INTENT_FOR_MODEL)?.let {
                model = it
                shimmerActive = true
                delay(2000)
                initializeView()
            } ?: kotlin.run {
                setUpObserver()
                initializeViewModel()
            }
        }
    }

    override fun initializeView() {
        super.initializeView()
        shimmerActive = false
        binding.viewShimmer.isVisible = false
        binding.parent.isVisible = true
        invalidateOptionsMenu()
        initializeData()
        initializeFragsView()
        calculateSum()
        initializeRecyclerView()
    }

    override fun initializeRecyclerView() {
        super.initializeRecyclerView()
        storyLogs(model.badges.toString())
        binding.badges.apply {
            if (model.badges.isNullOrEmpty()) {
                isVisible = false
            } else {
                isVisible = true
                this.layoutManager =
                    LinearLayoutManager(this@PreviewActivity, LinearLayoutManager.HORIZONTAL, false)
                this.adapter = BadgeAdapter(model.badges ?: emptyList())
                isNestedScrollingEnabled = false
            }
        }
    }

    override fun setUpObserver() {
        super.setUpObserver()
        observe(viewModel.getByIdLiveData) {
            this.model = it
            initializeView()
        }
    }

    override fun initializeViewModel() {
        super.initializeViewModel()
        intent.getStringExtra(IntentKeys.INTENT_FOR_ID)?.let {
            viewModel.selectById(it)
        } ?: run {
            finish()
        }
    }

    private fun calculateSum() {
        with(binding) {
            parentAction.isVisible = true
            buttonCart.isEnabled = model.isAddToCartEnable ?: true
            buttonCart.setString(model.addToCartButtonText)
            if (MyApplication.itemCount == 0) {
                buttonPay.visibility = View.INVISIBLE
            } else {
                buttonPay.visibility = View.VISIBLE
                buttonPay.setString(
                    GetString.BUTTON_PAY_COLON,
                    extra = "₹${String.format("%.1f", MyApplication.cartTotal)}"
                )
            }
        }
    }

    override fun initializeData() {
        super.initializeData()
        with(binding) {
            image.loadImage(model.imageURL ?: "", R.drawable.placeholder_one)
            price.setString(model.saleUnitPrice)
            title.setString(model.title ?: "")
            description.setString(model.brand ?: "")
            parentSupplier.alpha = if (model.isDirectFromSupplier!!) 0.5f else 1f
            supplier.setString(GetString.LABEL_SUPPLIER)
            parentFindMe.alpha = if (model.isFindMeEnable!!) 0.5f else 1f
            if (model.ratingCount == 0.toDouble()) {
                rating.setString(GetString.LABEL_FIRST_RATING)
            } else
                rating.setString(
                    GetString.LABEL_RATING,
                    prefix = "(${String.format("%.1f", model.ratingCount)}",
                    extra = ")"
                )
            findMe.setString(GetString.LABEL_FIND_ME)
            imageFindMe.setImageResource(
                if (model.isFindMeEnable != true)
                    R.drawable.icon_vd_find_me
                else R.drawable.icon_vd_find_me_off
            )
            calculateSum()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_preview, menu)
        menu?.findItem(R.id.menu_bookmark)?.apply {
            title = remoteViewModel.fetchString(GetString.ICON_DESC_BOOKMARK)
            titleCondensed = remoteViewModel.fetchString(GetString.ICON_DESC_BOOKMARK)
            isVisible = false
            setIcon(if (model.bookmark!!) R.drawable.icon_vd_fav_filled else R.drawable.icon_vd_fav)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.menu_bookmark)?.isVisible = !shimmerActive
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_bookmark) {
            model.bookmark = !model.bookmark!!
            item.setIcon(if (model.bookmark!!) R.drawable.icon_vd_fav_filled else R.drawable.icon_vd_fav)
            viewModel.updateBookmark(
                ProductBookmarkRequest(
                    model.id, model.bookmark
                )
            )
            setResult(
                RESULT_OK, Intent()
                    .putExtra(IntentKeys.INTENT_FOR_MODEL, model)
                    .putExtra(
                        IntentKeys.INTENT_FOR_POSITION,
                        intent.getIntExtra(IntentKeys.INTENT_FOR_POSITION, 0)
                    )
            )
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initializeFragsView() {
        super.initializeFragsView()
        supportFragmentManager.commit {
            replace(R.id.frag, ProductListFrag.getInstance(ProductListFrag.TYPE_OTHER))
            setReorderingAllowed(false)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onResult(requestCode, resultCode, data)
        if (requestCode == ConnectionActivity.REQUEST_CODE
            && resultCode == RESULT_OK
        ) {
            init()
        } else {
            finish()
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.button_cart) {
            ItemsSheet().showDialog(
                supportFragmentManager,
                model
            ) {
                calculateSum()
            }
        }
    }
}