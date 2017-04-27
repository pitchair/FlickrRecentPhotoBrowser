package com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules;

import android.content.Context;

import com.pitchai.flickrrecentphotobrowser.data.FlickrRestApiService;
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread;
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor;
import com.pitchai.flickrrecentphotobrowser.presentation.adapter.RecyclerPhotoAdapter;
import com.pitchai.flickrrecentphotobrowser.presentation.fragments.MainGridFragmentList;
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.PerFragment;
import com.pitchai.flickrrecentphotobrowser.presentation.presenter.GridPhotoListPresenterImp;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pitchairajamani on 4/24/17.
 */
@Module
public class MainGridListFragmentModule {
    MainGridFragmentList mainGridFragmentList;

    public MainGridListFragmentModule(MainGridFragmentList mainGridFragmentList) {
        this.mainGridFragmentList = mainGridFragmentList;
    }

    @Provides
    @PerFragment
    RecyclerPhotoAdapter ProvidesRecyclerPhotoAdapter(Context context, ThreadExecutor executor,
                                                      PostExecutionThread uiThread) {
        return new RecyclerPhotoAdapter(context, executor, uiThread);
    }

    @Provides
    @PerFragment
    GridPhotoListPresenterImp ProvidesGridPhotoListPresenterImp(FlickrRestApiService apiService,
                                                                ThreadExecutor executor,
                                                                PostExecutionThread uiThread) {
        return new GridPhotoListPresenterImp(apiService, executor, uiThread);
    }
}
