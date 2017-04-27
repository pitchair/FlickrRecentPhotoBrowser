package com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules;

import android.content.Context;

import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread;
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor;
import com.pitchai.flickrrecentphotobrowser.presentation.fragments.PhotoFragment;
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.PerFragment;
import com.pitchai.flickrrecentphotobrowser.presentation.presenter.GridPhotoImagePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pitchairajamani on 4/27/17.
 */
@Module
public class PhotoFragmentModule {
    PhotoFragment photoFragment;

    public PhotoFragmentModule(PhotoFragment photoFragment) {
        this.photoFragment = photoFragment;
    }

    @Provides
    @PerFragment
    GridPhotoImagePresenter providesGridPhotoImagePresenter(Context context, ThreadExecutor
            executor, PostExecutionThread postExecutionThread) {
        return new GridPhotoImagePresenter(context, executor, postExecutionThread);
    }
}
