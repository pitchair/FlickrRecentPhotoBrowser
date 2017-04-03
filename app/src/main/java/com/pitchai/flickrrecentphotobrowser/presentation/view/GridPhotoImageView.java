package com.pitchai.flickrrecentphotobrowser.presentation.view;

import android.graphics.Bitmap;

/**
 * Created by pitchairajamani on 3/23/17.
 */

public interface GridPhotoImageView {
    void renderPhotoImageView(Bitmap bitmap);
    void renderError(String Error);
}
