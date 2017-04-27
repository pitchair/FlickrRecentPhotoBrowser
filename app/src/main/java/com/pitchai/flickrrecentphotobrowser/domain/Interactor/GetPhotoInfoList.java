package com.pitchai.flickrrecentphotobrowser.domain.Interactor;

import com.pitchai.flickrrecentphotobrowser.Constants;
import com.pitchai.flickrrecentphotobrowser.data.FlickrRestApiService;
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.PhotoInfo;
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread;
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor;

import io.reactivex.Observable;

/**
 * Created by pitchairajamani on 3/2/17.
 */

public class GetPhotoInfoList extends UseCase<PhotoInfo,GetPhotoInfoList.Params> {

    FlickrRestApiService flickrRestApiService;

    public GetPhotoInfoList(FlickrRestApiService apiService, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor,postExecutionThread);
        this.flickrRestApiService = apiService;
    }

    @Override
    Observable<PhotoInfo> buildUseCaseObservable(Params params) {
        return flickrRestApiService.getRecentPhotoList(Constants.METHOD_GET_RECENT, Constants
                .NO_CALL_BACK, Constants.FORMAT, Constants.API_KEY, params.position, Constants
                .EXTRAS);
    }

    public static final class Params {
        private int position;

        private Params(int position) {
            this.position = position;
        }

        public static Params forPhotoList(int position) {
            return new Params(position);
        }

    }
}
