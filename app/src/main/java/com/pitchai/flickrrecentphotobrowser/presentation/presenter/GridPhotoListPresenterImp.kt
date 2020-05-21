package com.pitchai.flickrrecentphotobrowser.presentation.presenter

import com.pitchai.flickrrecentphotobrowser.data.FlickrRestApiService
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.PhotoInfo
import com.pitchai.flickrrecentphotobrowser.domain.Interactor.DefaultObserver
import com.pitchai.flickrrecentphotobrowser.domain.Interactor.GetPhotoInfoList
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor
import com.pitchai.flickrrecentphotobrowser.presentation.view.GridPhotoListView
import java.util.*

/**
 * Created by pitchairajamani on 2/27/17.
 */
class GridPhotoListPresenterImp(apiService: FlickrRestApiService, executor: ThreadExecutor, uiThread: PostExecutionThread) : Presenter {

    var gridPhotoListView1 : GridPhotoListView ? = null

    private var getPhotoInfoList: GetPhotoInfoList ? = null

    fun setGridPhotoListView(view: GridPhotoListView?) {
        gridPhotoListView1 = view!!
    }

    fun execute(currentPageNumber: Int) {
        getPhotoInfoList?.execute(PhotoInfoListObserver(), GetPhotoInfoList.Params.Companion.forPhotoList(currentPageNumber))
        gridPhotoListView1?.showLoading()
    }

    override fun resume() {}
    override fun pause() {}
    override fun destroy() {
        getPhotoInfoList?.dispose()
        gridPhotoListView1 = null
    }

    private inner class PhotoInfoListObserver : DefaultObserver<PhotoInfo?>() {
        override fun onNext(photoInfo: PhotoInfo?) {
            // no-op by default.
            var photoArrayList: ArrayList<Photo?>? = null
            var currentPageNumber = 0
            if (photoInfo != null) {
                val photos = photoInfo.photos
                val mTotalPage = photos?.pages ?: 0
                if (photos != null && mTotalPage > 0) {
                    currentPageNumber = photos.page
                    photoArrayList = ArrayList()
                    val photoList = photos.photo
                    for (photo in photoList) {
                        photoArrayList.add(photo)
                    }
                }
            }
            gridPhotoListView1?.hideLoading()
            gridPhotoListView1?.renderPhotoList(photoArrayList, currentPageNumber)
        }

        override fun onComplete() {
            // no-op by default.
        }

        override fun onError(exception: Throwable) {
            // no-op by default.
            gridPhotoListView1?.hideLoading()
            gridPhotoListView1?.showError(exception.message)
        }
    }

    init {
        getPhotoInfoList = GetPhotoInfoList(apiService, executor, uiThread)
    }
}