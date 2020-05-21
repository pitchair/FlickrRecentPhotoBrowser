package com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules

import android.content.Context
import com.pitchai.flickrrecentphotobrowser.data.FlickrRestApiService
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor
import com.pitchai.flickrrecentphotobrowser.presentation.adapter.RecyclerPhotoAdapter
import com.pitchai.flickrrecentphotobrowser.presentation.fragments.MainGridFragmentList
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.PerFragment
import com.pitchai.flickrrecentphotobrowser.presentation.presenter.GridPhotoListPresenterImp
import dagger.Module
import dagger.Provides

/**
 * Created by pitchairajamani on 4/24/17.
 */
@Module
class MainGridListFragmentModule(var mainGridFragmentList: MainGridFragmentList) {
    @Provides
    @PerFragment
    fun ProvidesRecyclerPhotoAdapter(context: Context, executor: ThreadExecutor,
                                     uiThread: PostExecutionThread): RecyclerPhotoAdapter {
        return RecyclerPhotoAdapter(context, executor, uiThread)
    }

    @Provides
    @PerFragment
    fun ProvidesGridPhotoListPresenterImp(apiService: FlickrRestApiService,
                                          executor: ThreadExecutor,
                                          uiThread: PostExecutionThread): GridPhotoListPresenterImp {
        return GridPhotoListPresenterImp(apiService, executor, uiThread)
    }

}