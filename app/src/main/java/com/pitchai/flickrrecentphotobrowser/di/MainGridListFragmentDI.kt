package com.pitchai.flickrrecentphotobrowser.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.pitchai.flickrrecentphotobrowser.presentation.fragments.MainGridFragmentList
import com.pitchai.flickrrecentphotobrowser.di.PerFragment
import com.pitchai.flickrrecentphotobrowser.presentation.ViewModel.PhotoListViewModel
import com.pitchai.flickrrecentphotobrowser.presentation.ViewModel.ViewModelFactory
import com.pitchai.flickrrecentphotobrowser.presentation.adapter.PhotoClickListener
import com.pitchai.flickrrecentphotobrowser.presentation.adapter.RecyclerPhotoListAdapter
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Created by pitchairajamani on 4/24/17.
 */
@InternalCoroutinesApi
@PerFragment
@Subcomponent(modules = [MainGridListFragmentModule::class])
interface MainGridListFragmentComponent {
    @OptIn(InternalCoroutinesApi::class)
    fun inject(mainGridFragmentList: MainGridFragmentList?): MainGridFragmentList?
    //fun inject(mainGridFragmentList: MainGridFragmentListOld?): MainGridFragmentListOld?
}

@InternalCoroutinesApi
@Module
class MainGridListFragmentModule (val mainGridFragmentList: MainGridFragmentList) {
    @Provides
    @PerFragment
    fun providesRecyclerPhotoListAdapter(context: Context): RecyclerPhotoListAdapter {
        return RecyclerPhotoListAdapter(context, mainGridFragmentList as PhotoClickListener)
    }

    @Provides
    @PerFragment
    fun providePhotoViewModel(viewModelFactory: ViewModelFactory) : PhotoListViewModel {
        return ViewModelProvider(mainGridFragmentList, viewModelFactory).get(PhotoListViewModel::class.java)
    }
}