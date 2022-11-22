package com.shopping.base

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.shopping.prefs.TempStorage
import com.shopping.prefs.UserStorage
import com.shopping.utilities.Utils
import com.shopping.variables.interfaces.BluePrint
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment : Fragment(), BluePrint.OfView, BluePrint.OfFrag,
    BluePrint.OfRecycler {
    @Inject
    lateinit var userStorage: UserStorage

    @Inject
    lateinit var tempStorage: TempStorage


    fun isUserStorageInitialized(): Boolean {
        return this::userStorage.isInitialized
    }

    /**
     * [uploading] variable hold value for Api Transaction. User [setUploadStatus] and [isUploading]
     * True if api transaction pending else false
     *
     */
    private var uploading: Boolean = false

    /**
     * [networkRequestPending] variable hold value for Api Transaction is pending due to network failure.
     * User [setUploadStatus] and [isUploading]
     * True if api transaction pending else false
     *
     */
    private var networkRequestPending: Boolean = false

    /**
     * [canLoadMore] provide data is more to load or not. Use [setCanLoadMore] and [canLoadMore]
     *
     */
    private var canLoadMore: Boolean = false

    /**
     * [changed] check user edit something so need to anounce before back
     *

     */
    private var changed: Boolean = false


    /**
     * [isAttached] will check is frag added or not
     */
    var isAttached: Boolean = false

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
                requireContext(),
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
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                onResult(requestCode, result.resultCode, result.data)
            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                onPermissionResult(requestPermissionCode!!, result)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isAttached = true
    }

    override fun setUpObserver() {

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

    override fun onDestroyView() {
        isAttached = false
        super.onDestroyView()
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
     * Update status of Api transaction by this
     */
    fun setNetworkRequestPending(networkRequestPending: Boolean) {
        this.networkRequestPending = networkRequestPending
    }

    /**
     * Get status of api transaction by this
     *
     */
    fun isNetworkRequestPending(): Boolean {
        return networkRequestPending
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
     * Maintaining status of Api call
     */
    fun hasConnection(): Boolean {
        networkRequestPending = Utils.isNetworkAvailable(requireContext())
        return networkRequestPending
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
}
