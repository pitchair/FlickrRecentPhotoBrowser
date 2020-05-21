package com.pitchai.flickrrecentphotobrowser.presentation.view

import android.content.Context

/**
 * Created by pitchairajamani on 2/27/17.
 */
interface LoadDataView {
    /**
     * Show a view with a progress bar indicating a loading process.
     */
    fun showLoading()

    /**
     * Hide a loading view.
     */
    fun hideLoading()

    /**
     * Show an error message
     *
     * @param message A string representing an error.
     */
    fun showError(message: String?)

    /**
     * Get a [Context].
     */
    fun context(): Context?
}