package com.pitchai.flickrrecentphotobrowser.presentation.internal.di.component;

import com.pitchai.flickrrecentphotobrowser.presentation.fragments.PhotoFragment;
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.PerFragment;
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules.PhotoFragmentModule;

import dagger.Subcomponent;

/**
 * Created by pitchairajamani on 4/27/17.
 */
@PerFragment
@Subcomponent(
        modules = PhotoFragmentModule.class
)
public interface PhotoFragmentComponent {
    PhotoFragment inject(PhotoFragment photoFragment);
}
