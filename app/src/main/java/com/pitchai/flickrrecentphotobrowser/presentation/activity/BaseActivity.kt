package com.pitchai.flickrrecentphotobrowser.presentation.activity

import android.app.Fragment
import android.os.Bundle
import android.support.v4.app.FragmentActivity

/**
 * Base [android.app.Activity] class for every Activity in this application.
 */
abstract class BaseActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.getApplicationComponent().inject(this);
    }

    /**
     * Adds a [Fragment] to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment The fragment to be added.
     */
    protected fun addFragment(containerViewId: Int, fragment: Fragment?) {
        val fragmentTransaction = this.fragmentManager.beginTransaction()
        fragmentTransaction.add(containerViewId, fragment)
        fragmentTransaction.commit()
    }
}