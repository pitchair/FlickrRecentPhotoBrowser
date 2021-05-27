package com.pitchai.flickrrecentphotobrowser.di

import androidx.lifecycle.ViewModelProvider
import com.pitchai.flickrrecentphotobrowser.presentation.activity.PhotoViewerActivity
import com.pitchai.flickrrecentphotobrowser.presentation.ViewModel.PhotoViewModel
import com.pitchai.flickrrecentphotobrowser.presentation.ViewModel.ViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Scope

@InternalCoroutinesApi
@ViewerActivityScope
@Subcomponent(modules = [PhotoViewerModule::class])
interface PhotoViewerComponent {
    fun inject(photoViewerActivity: PhotoViewerActivity): PhotoViewerActivity
    //fun inject(photoPagerFragment: PhotoPagerFragment): PhotoPagerFragment
    fun plus(module: PhotoFragmentModule?): PhotoFragmentComponent
}

@InternalCoroutinesApi
@Module
class PhotoViewerModule (val photoViewerActivity: PhotoViewerActivity) {
    //Anything to be provided
    @Provides
    @ViewerActivityScope
    fun providePhotoViewModel(viewModelFactory: ViewModelFactory) : PhotoViewModel {
        return ViewModelProvider(photoViewerActivity, viewModelFactory).get(PhotoViewModel::class.java)
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewerActivityScope