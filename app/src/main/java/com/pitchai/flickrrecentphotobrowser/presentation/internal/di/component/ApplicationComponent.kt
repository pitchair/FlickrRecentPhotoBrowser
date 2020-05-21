package com.pitchai.flickrrecentphotobrowser.presentation.internal.di.component

import com.pitchai.flickrrecentphotobrowser.data.Internal.di.modules.FlickrApiModule
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules.ApplicationModule
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules.MainGridListFragmentModule
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules.PhotoFragmentModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by pitchairajamani on 2/22/17.
 */
@Singleton
@Component(modules = [ApplicationModule::class, FlickrApiModule::class])
interface ApplicationComponent {
    //void inject(MainGridFragmentList mainGridFragmentList);
    operator fun plus(module: MainGridListFragmentModule?): MainGridListFragmentComponent
    operator fun plus(module: PhotoFragmentModule?): PhotoFragmentComponent
}