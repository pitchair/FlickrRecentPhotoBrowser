package com.pitchai.flickrrecentphotobrowser.utils

import android.app.Activity
import android.app.AlertDialog
import android.util.DisplayMetrics
import android.util.Log
import com.pitchai.flickrrecentphotobrowser.R
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo

/**
 * Created by pgrajama on 12/10/16.
 */
object Utils {
    fun getUrl(photo: Photo?): String {
        val baseAddress = "staticflickr.com"
        val url = "https://farm" + photo?.farm + "." + baseAddress + "/" + photo?.server +
                "/" + photo?.id + "_" + photo?.secret + ".jpg"
        Log.d("url", "url: $url")
        return url
    }

    fun createAlertDialog(activity: Activity?, message: String?, title: String?): AlertDialog {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(R.string.ok, null)
        return builder.create()
    }

    fun createAlertDialog(activity: Activity?, message: String?, title: String?,
                          postiveResID: Int, negativeResID: Int, callback: DialogCallback?): AlertDialog {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(postiveResID) { dialogInterface, i -> callback?.positiveButtonCallback() }
                .setNegativeButton(negativeResID) { dialogInterface, i -> callback?.negativeButtonCallback() }
        return builder.create()
    }

    fun calculateWidth(activity: Activity): Int {
        val localDisplayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(localDisplayMetrics)
        val pixelSize = activity.resources.getDimensionPixelSize(R.dimen.grid_photo_size)
        val widthScale = localDisplayMetrics.widthPixels / pixelSize
        return localDisplayMetrics.widthPixels / widthScale
    }

    fun calculateWidthScale(activity: Activity): Int {
        val localDisplayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(localDisplayMetrics)
        val pixelSize = activity.resources.getDimensionPixelSize(R.dimen.grid_photo_size)
        return localDisplayMetrics.widthPixels / pixelSize
    }

    interface DialogCallback {
        fun positiveButtonCallback()
        fun negativeButtonCallback()
    }
}