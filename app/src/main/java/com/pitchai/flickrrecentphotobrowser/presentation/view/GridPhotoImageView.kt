package com.pitchai.flickrrecentphotobrowser.presentation.view

import android.graphics.Bitmap

/**
 * Created by pitchairajamani on 3/23/17.
 */
interface GridPhotoImageView {
    fun renderPhotoImageView(bitmap: Bitmap?)
    fun renderError(Error: String?)
}