package com.shopping.ui.activity

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.shopping.R
import com.shopping.base.BaseActivity
import com.shopping.base.observe
import com.shopping.databinding.ActivityMainBinding
import com.shopping.jetpacks.extensions.setString
import com.shopping.jetpacks.viewmodels.HomeVM
import com.shopping.ui.frag.ProductListFrag
import com.shopping.utilities.BD
import com.shopping.utilities.storyLogs
import com.shopping.variables.enums.GetString
import com.shopping.variables.enums.ToolbarTitle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    private val viewModels by viewModels<HomeVM>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var menuItem: MenuItem
    private var isBackEnable: Boolean = false
    private var currentPageCache: Int = 0
    private var lastPageCache: Int = 0
    private var doubleBackToExitPressedOnce = false
    private val handler = Handler(Looper.getMainLooper())
    private val backPressRunnable = Runnable { doubleBackToExitPressedOnce = false }
    private var lastBookmarkCount = 0
    private var cartCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setToolbar(remoteViewModel.fetchString(ToolbarTitle.HOME))
        initializeFragsView()
        setUpObserver()
    }

    override fun setToolbar(title: String?) {
        super.setToolbar(title)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun setUpObserver() {
        observe(viewModels.cartCountLiveData) {
            cartCount = it
            updateCartBadge()
        }
    }

    override fun initializeFragsView() {
        super.initializeFragsView()
        binding.idBottomNavigation.setOnItemSelectedListener { menuItem ->
            return@setOnItemSelectedListener when (menuItem.itemId) {
                R.id.menu_product -> {
                    binding.viewPager.currentItem = HOME
                    true
                }
                R.id.menu_bookmark -> {
                    binding.viewPager.currentItem = BOOKMARK
                    true
                }
                else -> false
            }
        }

        binding.idBottomNavigation.menu.findItem(R.id.menu_product).title =
            remoteViewModel.fetchString(ToolbarTitle.HOME)
        binding.idBottomNavigation.menu.findItem(R.id.menu_bookmark).title =
            remoteViewModel.fetchString(ToolbarTitle.BOOKMARK)
        binding.viewPager.adapter = ManageAdapter(supportFragmentManager)
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (!isBackEnable) {
                    if (currentPageCache != lastPageCache) {
                        lastPageCache = currentPageCache
                    }
                    currentPageCache = binding.viewPager.currentItem
                }
                isBackEnable = false
                setUpBottomThings(position)
                invalidateOptionsMenu()
                when (position) {
                    BOOKMARK -> {
                        supportActionBar?.title = remoteViewModel.fetchString(ToolbarTitle.BOOKMARK)
                        BD.setBadgeCount(
                            this@HomeActivity,
                            0,
                            R.id.ic_badge_bookmark,
                            binding.idBottomNavigation.menu.findItem(R.id.menu_bookmark)
                                .icon as LayerDrawable
                        )
                        lastBookmarkCount = 0
                        binding.idBottomNavigation.menu.findItem(R.id.menu_bookmark)
                            .setIcon(R.drawable.icon_vd_fav_list_filled)
                    }
                    else -> {
                        binding.idBottomNavigation.menu.findItem(R.id.menu_bookmark)
                            .setIcon(R.drawable.ic_menu_bookmark)
                        supportActionBar?.setTitle(remoteViewModel.fetchString(ToolbarTitle.HOME))
                    }
                }
            }
        })
    }

    private fun setUpBottomThings(position: Int) {
        when (position) {
            HOME -> {
                binding.idBottomNavigation.selectedItemId = R.id.menu_product
            }
            else -> {
                binding.idBottomNavigation.selectedItemId = R.id.menu_bookmark
            }
        }
    }

    private inner class ManageAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            val frag = ProductListFrag.getInstance(position)
            if (position == 1) {
                frag.bindListener { action, _ ->
                    if (action == 0)
                        binding.viewPager.currentItem = HOME
                }
            } else {
                frag.bindListener { _, it ->
                    lastBookmarkCount = if (it) lastBookmarkCount + 1 else lastBookmarkCount - 1
                    storyLogs("$it  $lastBookmarkCount")
                    BD.setBadgeCount(
                        this@HomeActivity,
                        lastBookmarkCount,
                        R.id.ic_badge_bookmark,
                        binding.idBottomNavigation.menu.findItem(R.id.menu_bookmark).icon as LayerDrawable
                    )
                }
            }
            return frag
        }

        override fun getCount(): Int {
            return 2
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        menu?.let {
            this.menuItem = it.findItem(R.id.menu_cart)
            this.menuItem.setString(GetString.ICON_DESC_RATE)
            it.findItem(R.id.menu_notification).setString(GetString.ICON_DESC_NOTIFICATION)
            it.findItem(R.id.menu_privacy).setString(GetString.ICON_DESC_PRIVACY)
            it.findItem(R.id.menu_cart).setString(GetString.ICON_DESC_CART)
            it.findItem(R.id.menu_clear_bookmark).also { item ->
                item.setString(GetString.ICON_DESC_CLEAR_BOOKMARK)
                item.isVisible = false
            }
            it.findItem(R.id.menu_rate).setString(GetString.ICON_DESC_RATE)
            it.findItem(R.id.menu_terms).setString(GetString.ICON_DESC_TERMS)
            it.findItem(R.id.menu_logout).setString(GetString.ICON_DESC_LOGOUT)
            BD.setBadgeCount(
                this,
                2,
                R.id.ic_badge_notification,
                it.findItem(R.id.menu_notification).icon as LayerDrawable
            )
            viewModels.fetchCount()
            updateCartBadge()
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.menu_notification)?.isVisible = binding.viewPager.currentItem == 0
        menu?.findItem(R.id.menu_clear_bookmark)?.isVisible = binding.viewPager.currentItem == 1
        updateCartBadge()
        return super.onPrepareOptionsMenu(menu)
    }


    private fun updateCartBadge() {
        if (this::menuItem.isInitialized)
            BD.setBadgeCount(
                this,
                cartCount,
                R.id.ic_badge_cart,
                menuItem.icon as LayerDrawable
            )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when {
            binding.viewPager.currentItem > HOME -> {
                isBackEnable = true
                if (lastPageCache == binding.viewPager.currentItem) {
                    lastPageCache = HOME
                }
                if (lastPageCache != HOME) {
                    binding.viewPager.currentItem = lastPageCache
                    lastPageCache = HOME
                } else
                    binding.viewPager.currentItem = HOME
            }
            else -> {
                if (!doubleBackToExitPressedOnce) {
                    this.doubleBackToExitPressedOnce = true
                    Toast.makeText(
                        this@HomeActivity,
                        remoteViewModel.fetchString(GetString.AGAIN_EXIT),
                        Toast.LENGTH_LONG
                    ).show()
                    handler.postDelayed(backPressRunnable, 2000)
                } else {
                    handler.removeCallbacks(backPressRunnable)
                    super.onBackPressed()
                }
            }
        }
    }

    companion object {
        private const val HOME = 0
        private const val BOOKMARK = 1
    }

}