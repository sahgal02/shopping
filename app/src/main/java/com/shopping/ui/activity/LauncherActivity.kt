package com.shopping.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.shopping.base.BaseActivity
import com.shopping.databinding.ActivityLauncherBinding

class LauncherActivity : BaseActivity() {
    private lateinit var binding: ActivityLauncherBinding
    private var handler: Handler = Handler(Looper.getMainLooper())
    private val runnable: Runnable = Runnable { homeStart() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initializeView()
        handler.postDelayed(runnable, 2000)
    }

    private fun homeStart() {
        if (hasConnection()) {
            launcher().launch(Intent(this, HomeActivity::class.java))
            finish()
        } else
            ConnectionActivity.launch(this)
    }

    override fun onResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onResult(requestCode, resultCode, data)
        if (requestCode == ConnectionActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            homeStart()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        closeEverything()
    }

    override fun closeEverything() {
        handler.removeCallbacks(runnable)
    }

}
