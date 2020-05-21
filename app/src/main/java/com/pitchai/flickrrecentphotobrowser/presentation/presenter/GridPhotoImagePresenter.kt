package com.pitchai.flickrrecentphotobrowser.presentation.presenter

import android.content.Context
import android.graphics.Bitmap
import com.pitchai.flickrrecentphotobrowser.domain.Interactor.DefaultObserver
import com.pitchai.flickrrecentphotobrowser.domain.Interactor.GetPhotoImage
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor
import com.pitchai.flickrrecentphotobrowser.presentation.view.GridPhotoImageView

/**
 * Created by pitchairajamani on 3/23/17.
 */
class GridPhotoImagePresenter(context: Context, threadExecutor: ThreadExecutor,
                              postExecutionThread: PostExecutionThread) : Presenter {
    private val photoImage: GetPhotoImage
    override fun resume() {}
    override fun pause() {}
    override fun destroy() {
        photoImage.dispose()
    }

    fun execute(view: GridPhotoImageView, param: GetPhotoImage.Params) {
        photoImage.execute(PhotoImageObserver(view) as DefaultObserver<Bitmap>, param)
    }

    private inner class PhotoImageObserver internal constructor(private val view: GridPhotoImageView) : DefaultObserver<Bitmap?>() {
        override fun onNext(bitmap: Bitmap?) {
            // no-op by default.
            view.renderPhotoImageView(bitmap)
        }

        override fun onComplete() {
            // no-op by default.
        }

        override fun onError(exception: Throwable) {
            // no-op by default.
            view.renderError(exception.message)
        }

    }

    init {
        photoImage = GetPhotoImage(context, threadExecutor, postExecutionThread)
    }
}