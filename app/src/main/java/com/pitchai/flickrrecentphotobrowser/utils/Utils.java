package com.pitchai.flickrrecentphotobrowser.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.Log;

import com.pitchai.flickrrecentphotobrowser.R;
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo;


/**
 * Created by pgrajama on 12/10/16.
 */

public class Utils {
    public interface DialogCallback {
        void positiveButtonCallback();

        void negativeButtonCallback();
    }

    public static String getUrl(Photo photo) {
        String baseAddress = "staticflickr.com";
        String url = "https://farm" + photo.getFarm() + "." + baseAddress + "/" + photo.getServer
                () +
                "/" + photo.getId() + "_" + photo.getSecret() + ".jpg";
        Log.d("url", "url: " + url);
        return url;
    }

    public static AlertDialog createAlertDialog(Activity activity, String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(R.string.ok, null);
        return builder.create();
    }

    public static AlertDialog createAlertDialog(Activity activity, String message, String title,
                                                int postiveResID, int negativeResID, final
                                                DialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(postiveResID, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (callback != null) {
                            callback.positiveButtonCallback();
                        }
                    }
                })
                .setNegativeButton(negativeResID, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (callback != null) {
                            callback.negativeButtonCallback();
                        }
                    }
                });
        return builder.create();
    }

    public static int calculateWidth(Activity activity) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int pixelSize = activity.getResources().getDimensionPixelSize(R.dimen.grid_photo_size);
        int widthScale = localDisplayMetrics.widthPixels / pixelSize;
        int width = (localDisplayMetrics.widthPixels / widthScale);
        return width;
    }

    public static int calculateWidthScale(Activity activity) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int pixelSize = activity.getResources().getDimensionPixelSize(R.dimen.grid_photo_size);
        int widthScale = localDisplayMetrics.widthPixels / pixelSize;
        return widthScale;
    }
}
