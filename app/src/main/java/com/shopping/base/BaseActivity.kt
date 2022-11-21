package com.shopping.base

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.shopping.R
import com.shopping.jetpacks.viewmodels.ConfigVM
import com.shopping.prefs.TempStorage
import com.shopping.prefs.UserStorage
import com.shopping.utilities.Utils
import com.shopping.variables.interfaces.BluePrint
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Open base class implementation to manage common task and have [BluePrint] implementation for Common Function structures
 *
 */
@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(), BluePrint.OfView, BluePrint.OfActivity,
    BluePrint.OfFrag, BluePrint.OfRecycler {

    val remoteViewModel by viewModels<ConfigVM>()

    @Inject
    lateinit var userStorage: UserStorage

    @Inject
    lateinit var tempStorage: TempStorage

    /**
     * [uploading] variable hold value for Api Transaction. User [setUploadStatus] and [isUploading]
     * True if api transaction pending else false
     *
     */
    private var uploading: Boolean = false

    /**
     * [canLoadMore] provide data is more to load or not. Use [setCanLoadMore] and [canLoadMore]
     *
     */
    private var canLoadMore: Boolean = false

    /**
     * [hasConnection] check the monitor connection changes status. Use by [hasConnection]
     *
     */
    private var hasConnection: Boolean = true

    /**
     * [changed] check user edit something so need to anounce before back
     *
     */
    private var changed: Boolean = false

    /**
     * [requestCode] for tracking [Result]
     */
    private var requestCode: Int = 0

    private var requestPermissionCode: String? = null

    private lateinit var launcher: ActivityResultLauncher<Intent>

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    /**
     * [launcher] is the new way to launch activities and call backs
     */
    fun requestPermission(requestPermissionCode: String): Boolean {
        this.requestPermissionCode = requestPermissionCode
        if (ContextCompat.checkSelfPermission(
                this,
                requestPermissionCode
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(requestPermissionCode)
            return false
        }
        return true
    }

    /**
     * [launcher] is the new way to launch activities and call backs
     */
    fun launcher(requestCode: Int? = 0): ActivityResultLauncher<Intent> {
        this.requestCode = requestCode!!
        return launcher
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasConnection = Utils.isNetworkAvailable(this)
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                onResult(requestCode, result.resultCode, result.data)
            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                onPermissionResult(requestPermissionCode!!, result)
            }
    }

    /**
     * [setToolbar] fill detail inside Toolbar, like title by [title]
     * Auto function to set app bar by [R.id.id_app_bar]
     *
     */
    override fun setToolbar(title: String?) {
        setToolbar(findViewById(R.id.id_app_bar), title, true)
    }

    /**
     * [setToolbar] fill detail inside Toolbar, like title by [title].
     * Auto function to set app bar by [toolbar]
     *
     */
    override fun setToolbar(toolbar: Toolbar, title: String?, statusColorWhite: Boolean?) {
        setSupportActionBar(toolbar)
        setSupportActionBar(supportActionBar, title, statusColorWhite)
    }

    private fun setSupportActionBar(
        actionBar: ActionBar?,
        title: String? = null,
        statusColorWhite: Boolean? = true
    ) {
        if (statusColorWhite!!)
            Utils.doStatusColorWhite(window)
        actionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            if (title != null) {
                it.title = title
                it.setDisplayShowTitleEnabled(true)
            } else {
                it.setDisplayShowTitleEnabled(false)
            }
        }
    }

    override fun setToolbar(toolbar: Toolbar, title: Int) {
    }

    /**
     * Only view initialization
     *
     */
    override fun initializeView() {

    }

    /**
     * All kind of listeners call here
     *
     */
    override fun initializeListeners() {

    }

    /**
     * Any kind of Pickers implementation
     *
     */
    override fun initializePicker() {

    }

    /**
     * Data going to be fill in UI comes here
     *
     */
    override fun initializeData() {

    }

    /**
     * User of [androidx.lifecycle.ViewModel] or if need initialization comes their
     *
     */
    override fun initializeViewModel() {

    }

    /**
     * In case of [com.google.android.material.tabs.TabLayout] and [androidx.viewpager.widget.ViewPager]
     *
     */
    override fun initializeTabView() {

    }

    /**
     * User for [androidx.recyclerview.widget.RecyclerView.Recycler] Implementation
     *
     */
    override fun initializeRecyclerView() {

    }

    /**
     * In case of [androidx.recyclerview.widget.RecyclerView.Recycler] we have function to maintain no data information view
     *
     */
    override fun initializeEmptyView(isEmpty: Boolean) {

    }

    /**
     * Any kind for Frag initialization of Adding
     *
     */
    override fun initializeFragsView() {

    }
    /**
     * Any kind for Frag initialization of Adding
     *
     */
    override fun setUpObserver() {

    }
    override fun onDestroy() {
        super.onDestroy()
        closeEverything()
    }

    /**
     * Function to call from [onDestroy] and on backpress to cancel threads or task running
     *
     */
    override fun closeEverything() {

    }

    /**
     * [connectionUpdate] override this method to maintain view bases of network configuration
     *
     */
    override fun connectionUpdate(hasConnection: Boolean) {
        this.hasConnection = hasConnection
        findViewById<TextView>(R.id.id_text_connection)?.visibility = if (hasConnection) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    /**
     * Update status of Api transaction by this
     */
    fun setUploadStatus(isUploading: Boolean) {
        this.uploading = isUploading
    }

    /**
     * Get status of api transaction by this
     *
     */
    fun isUploading(): Boolean {
        return uploading
    }

    /**
     * Check something is changed or not
     *
     */
    fun isChanged(): Boolean {
        return changed
    }

    /**
     * Update load more availability by this
     */
    fun setChanged(changed: Boolean) {
        this.changed = changed
    }


    /**
     * Update load more availability by this
     */
    fun setCanLoadMore(canLoadMore: Boolean) {
        this.canLoadMore = canLoadMore
    }

    /**
     * Get status of load more availability by this
     */
    fun canLoadMore(): Boolean {
        return canLoadMore
    }

    override fun onStart() {
        super.onStart()
        setNetworkIntent()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    /**
     * Network monitoring intent call
     */
    private fun setNetworkIntent() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    /**
     * Broadcaster for monitoring and status update on network changes
     */
    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            connectionUpdate(Utils.isNetworkAvailable(this@BaseActivity))
        }
    }

    /**
     * Get status update of Connection by [hasConnection]
     */
    fun hasConnection(): Boolean {
        if (!hasConnection) {
           // Utils.snackbar(this, getString(R.string.string_message_check_connection))
        }
        return this.hasConnection
    }

    /**
     * Result callback for an Activity Launcher
     * [requestCode] : used to check used for
     * [resultCode] : [Activity.RESULT_OK], [Activity.RESULT_CANCELED]
     * [data] : [Intent] in bundle data
     */
    override fun onResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    /**
     * Result callback for permission request
     * [granted] : Granted or denied
     */
    override fun onPermissionResult(requestCode: String, isGranted: Boolean) {

    }

    override fun onBackPressed() {
        closeEverything()
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}
