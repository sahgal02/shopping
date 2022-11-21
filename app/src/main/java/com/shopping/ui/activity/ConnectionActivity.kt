package com.shopping.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.shopping.R
import com.shopping.base.BaseActivity
import com.shopping.databinding.ActivityConnectionBinding
import com.shopping.utilities.Utils
import dagger.hilt.android.AndroidEntryPoint

/**
 * Check connection and show view when no internet available
 */
@AndroidEntryPoint
class ConnectionActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityConnectionBinding
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = {
        if (hasConnection()) {
            setResult(RESULT_OK)
        } else {
            binding.buttonSubmit.setTextColor(
                ContextCompat.getColor(this, R.color.colorWhite)
            )
            binding.buttonSubmit.isVisible = true
            binding.root.findViewById<ProgressBar>(R.id.id_progress_bar).isVisible = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectionBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.clickListener = this
        Utils.doStatusColorWhite(window)
    }

    override fun onClick(v: View?) {
        handler.postDelayed(runnable, 1500)
    }

    /**
     * Function up-to-date you with Network
     */
    override fun connectionUpdate(hasConnection: Boolean) {
        super.connectionUpdate(hasConnection)
        binding.buttonSubmit.isVisible = false
        binding.root.findViewById<ProgressBar>(R.id.id_progress_bar).isVisible = true
        onClick(null)
    }

    override fun onBackPressed() {
        handler.removeCallbacks(runnable)
        setResult(RESULT_CANCELED)
        super.onBackPressed()
    }

    companion object {
        fun launch(context: BaseActivity) {
            context.launcher(REQUEST_CODE).launch(Intent(context,
                    ConnectionActivity::class.java), )
        }

        const val REQUEST_CODE = 111;
    }
}