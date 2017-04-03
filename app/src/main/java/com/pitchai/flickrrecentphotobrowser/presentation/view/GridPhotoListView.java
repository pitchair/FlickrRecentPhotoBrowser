package com.pitchai.flickrrecentphotobrowser.presentation.view;

import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo;

import java.util.List;

/**
 * Created by pitchairajamani on 2/27/17.
 */

public interface GridPhotoListView extends LoadDataView{
    void renderPhotoList(List<Photo> photoList, int currentPageNumber);
}
