package com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules

import android.content.Context
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor
import com.pitchai.flickrrecentphotobrowser.presentation.fragments.PhotoFragment
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.PerFragment
import com.pitchai.flickrrecentphotobrowser.presentation.presenter.GridPhotoImagePresenter
import dagger.Module
import dagger.Provides

/**
 * Created by pitchairajamani on 4/27/17.
 */
@Module
class PhotoFragmentModule(var photoFragment: PhotoFragment) {
    @Provides
    @PerFragment
    fun providesGridPhotoImagePresenter(context: Context, executor: ThreadExecutor, postExecutionThread: PostExecutionThread): GridPhotoImagePresenter {
        return GridPhotoImagePresenter(context, executor, postExecutionThread)
    }

}