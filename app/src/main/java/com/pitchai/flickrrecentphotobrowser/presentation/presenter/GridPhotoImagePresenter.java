package com.pitchai.flickrrecentphotobrowser.presentation.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.pitchai.flickrrecentphotobrowser.domain.Interactor.DefaultObserver;
import com.pitchai.flickrrecentphotobrowser.domain.Interactor.GetPhotoImage;
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread;
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor;
import com.pitchai.flickrrecentphotobrowser.presentation.view.GridPhotoImageView;

/**
 * Created by pitchairajamani on 3/23/17.
 */

public class GridPhotoImagePresenter implements Presenter{

    private GetPhotoImage photoImage;

    public GridPhotoImagePresenter(Context context, ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread) {
        photoImage = new GetPhotoImage(context, threadExecutor, postExecutionThread);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        photoImage.dispose();
    }

    public void execute(GridPhotoImageView
                                view, GetPhotoImage.Params param) {
        photoImage.execute(new PhotoImageObserver(view), param);
    }

    private final class PhotoImageObserver extends DefaultObserver<Bitmap> {

        private GridPhotoImageView view;

        PhotoImageObserver(GridPhotoImageView view) {
            this.view = view;
        }
        @Override
        public void onNext(Bitmap bitmap) {
            // no-op by default.
            view.renderPhotoImageView(bitmap);
        }

        @Override
        public void onComplete() {
            // no-op by default.
        }

        @Override
        public void onError(Throwable exception) {
            // no-op by default.
            view.renderError(exception.getMessage());
        }
    }
}
