package com.pitchai.flickrrecentphotobrowser.presentation.internal.di.component;

import com.pitchai.flickrrecentphotobrowser.data.Internal.di.modules.FlickrApiModule;
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules.ApplicationModule;
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules.MainGridListFragmentModule;


import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules.PhotoFragmentModule;


import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by pitchairajamani on 2/22/17.
 */
@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                FlickrApiModule.class
        }
)
public interface ApplicationComponent {
        //void inject(MainGridFragmentList mainGridFragmentList);
        MainGridListFragmentComponent plus(MainGridListFragmentModule module);
        PhotoFragmentComponent plus(PhotoFragmentModule module);
}
