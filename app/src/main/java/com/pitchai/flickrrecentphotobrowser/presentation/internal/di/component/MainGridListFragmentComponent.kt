package com.pitchai.flickrrecentphotobrowser.presentation.internal.di.component

import com.pitchai.flickrrecentphotobrowser.presentation.fragments.MainGridFragmentList
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.PerFragment
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules.MainGridListFragmentModule
import dagger.Subcomponent

/**
 * Created by pitchairajamani on 4/24/17.
 */
@PerFragment
@Subcomponent(modules = [MainGridListFragmentModule::class])
interface MainGridListFragmentComponent {
    fun inject(mainGridFragmentList: MainGridFragmentList?): MainGridFragmentList?
}