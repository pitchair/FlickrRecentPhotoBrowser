package com.pitchai.flickrrecentphotobrowser.di

import com.pitchai.flickrrecentphotobrowser.presentation.fragments.PhotoFragment
import com.pitchai.flickrrecentphotobrowser.di.PerFragment
import dagger.Module
import dagger.Subcomponent
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Created by pitchairajamani on 4/27/17.
 */
@InternalCoroutinesApi
@PerFragment
@Subcomponent(modules = [PhotoFragmentModule::class])
interface PhotoFragmentComponent {
    fun inject(photoFragment: PhotoFragment?): PhotoFragment?
}

@Module
@InternalCoroutinesApi
class PhotoFragmentModule(var photoFragment: PhotoFragment) {
}