package com.pitchai.flickrrecentphotobrowser.domain.Interactor

import com.pitchai.flickrrecentphotobrowser.Constants
import com.pitchai.flickrrecentphotobrowser.data.FlickrRestApiService
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.PhotoInfo
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor
import io.reactivex.Observable

/**
 * Created by pitchairajamani on 3/2/17.
 */
class GetPhotoInfoList(var flickrRestApiService: FlickrRestApiService, threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread) : UseCase<PhotoInfo?, GetPhotoInfoList.Params>(threadExecutor, postExecutionThread) {
    public override fun buildUseCaseObservable(params: Params): Observable<PhotoInfo?>? {
        return flickrRestApiService.getRecentPhotoList(Constants.METHOD_GET_RECENT, Constants.NO_CALL_BACK, Constants.FORMAT, Constants.API_KEY, params.position, Constants.EXTRAS)
    }

    class Params private constructor(val position: Int) {

        companion object {
            fun forPhotoList(position: Int): Params {
                return Params(position)
            }
        }

    }

}