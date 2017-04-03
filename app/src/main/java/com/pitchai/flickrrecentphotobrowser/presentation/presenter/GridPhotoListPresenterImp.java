package com.pitchai.flickrrecentphotobrowser.presentation.presenter;

import com.pitchai.flickrrecentphotobrowser.data.RestApiService;
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo;
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.PhotoInfo;
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photos;
import com.pitchai.flickrrecentphotobrowser.domain.Interactor.DefaultObserver;
import com.pitchai.flickrrecentphotobrowser.domain.Interactor.GetPhotoInfoList;
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread;
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor;
import com.pitchai.flickrrecentphotobrowser.presentation.view.GridPhotoListView;

import java.util.ArrayList;

/**
 * Created by pitchairajamani on 2/27/17.
 */

public class GridPhotoListPresenterImp implements Presenter {

    GridPhotoListView gridPhotoListView;
    private final GetPhotoInfoList getPhotoInfoList;

    public GridPhotoListPresenterImp(RestApiService apiService, ThreadExecutor executor, PostExecutionThread uiThread,
                                     GridPhotoListView gridPhotoListView) {
        this.gridPhotoListView = gridPhotoListView;
        getPhotoInfoList = new GetPhotoInfoList(apiService,executor,uiThread);
    }

    public void execute(int currentPageNumber) {
        getPhotoInfoList.execute(new PhotoInfoListObserver(), GetPhotoInfoList.Params.forPhotoList(currentPageNumber));
        gridPhotoListView.showLoading();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        getPhotoInfoList.dispose();
        gridPhotoListView = null;
    }

    private  final class PhotoInfoListObserver extends DefaultObserver<PhotoInfo> {
        @Override public void onNext(PhotoInfo photoInfo) {
            // no-op by default.
            ArrayList<Photo> photoArrayList = null;
            int currentPageNumber = 0;
            if (photoInfo != null) {
                Photos photos = photoInfo.getPhotos();
                int mTotalPage = photos.getPages();
                if (photos != null && mTotalPage > 0) {
                    currentPageNumber = photos.getPage();
                    photoArrayList = new ArrayList<>();
                    Photo[] photoList = photos.getPhoto();
                    for (Photo photo : photoList) {
                        photoArrayList.add(photo);
                    }
                }
            }
            gridPhotoListView.hideLoading();
            gridPhotoListView.renderPhotoList(photoArrayList,currentPageNumber);
        }

        @Override public void onComplete() {
            // no-op by default.
        }

        @Override public void onError(Throwable exception) {
            // no-op by default.
            gridPhotoListView.hideLoading();
            gridPhotoListView.showError(exception.getMessage());
        }
    }
}
