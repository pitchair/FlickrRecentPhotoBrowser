package com.pitchai.flickrrecentphotobrowser.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.pitchai.flickrrecentphotobrowser.AndroidApplication
import com.pitchai.flickrrecentphotobrowser.data.api.FlickrRestApiService
import com.pitchai.flickrrecentphotobrowser.data.db.PhotoDataBase
import com.pitchai.flickrrecentphotobrowser.data.source.PhotoRepo
import com.pitchai.flickrrecentphotobrowser.data.source.PhotoRepositoryImp
import com.pitchai.flickrrecentphotobrowser.presentation.ViewModel.ViewModelFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

/**
 * Created by pitchairajamani on 2/22/17.
 */
@Singleton
@Component(modules = [ApplicationModule::class])
@InternalCoroutinesApi
interface ApplicationComponent {
    operator fun plus(module: MainGridListFragmentModule?): MainGridListFragmentComponent
    operator fun plus(module: PhotoViewerModule): PhotoViewerComponent
}

@InternalCoroutinesApi
@Module
class ApplicationModule(private val application: AndroidApplication) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun providesPhotoDataBase(context: Context) : PhotoDataBase {
        return PhotoDataBase.getInstance(context)
    }

    @Provides
    @Singleton
    fun providesPhotoRepo( photoDataBase: PhotoDataBase) : PhotoRepo {
        return PhotoRepositoryImp(FlickrRestApiService.create(), photoDataBase)
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    @Provides
    @Singleton
    fun provideViewModelFactory(photoRepo: PhotoRepo): ViewModelProvider.Factory {
        return ViewModelFactory(photoRepo)
    }
}