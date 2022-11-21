package com.shopping.variables.interfaces

import android.content.Intent
import androidx.appcompat.widget.Toolbar

interface BluePrint {

    interface OfActivity {
        fun setToolbar(title: String? = null)

        fun setToolbar(toolbar: Toolbar, title: String? = null, statusColorWhite: Boolean? = true)

        fun setToolbar(toolbar: Toolbar, title: Int)

        fun connectionUpdate(hasConnection: Boolean)
    }


    interface OfView {
        fun initializeView()

        fun initializeListeners()

        fun initializeData()

        fun initializePicker()

        fun initializeViewModel()

        fun initializeTabView()

        fun setUpObserver()

        fun closeEverything()

        fun onResult(requestCode: Int, resultCode: Int, data: Intent?)

        fun onPermissionResult(requestCode: String, isGranted: Boolean)
    }

    interface OfRecycler {
        fun initializeRecyclerView()

        fun initializeEmptyView(isEmpty: Boolean)
    }

    interface OfFrag {
        fun initializeFragsView()
    }
}