package com.pitchai.flickrrecentphotobrowser.presentation.view

import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo

/**
 * Created by pitchairajamani on 2/27/17.
 */
interface GridPhotoListView : LoadDataView {
    fun renderPhotoList(photoList: List<Photo?>?, currentPageNumber: Int)
}