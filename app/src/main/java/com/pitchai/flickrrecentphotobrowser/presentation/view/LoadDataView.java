package com.pitchai.flickrrecentphotobrowser.presentation.view;

import android.content.Context;

/**
 * Created by pitchairajamani on 2/27/17.
 */

public interface LoadDataView {

    /**
     * Show a view with a progress bar indicating a loading process.
     */
    void showLoading();

    /**
     * Hide a loading view.
     */
    void hideLoading();

    /**
     * Show an error message
     *
     * @param message A string representing an error.
     */
    void showError(String message);

    /**
     * Get a {@link Context}.
     */
    Context context();
}
