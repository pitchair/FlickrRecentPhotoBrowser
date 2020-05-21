package com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules

import android.content.Context
import com.pitchai.flickrrecentphotobrowser.AndroidApplication
import com.pitchai.flickrrecentphotobrowser.domain.JobExecutor
import com.pitchai.flickrrecentphotobrowser.domain.UIThread
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by pitchairajamani on 4/4/17.
 */
@Module
class ApplicationModule(private val application: AndroidApplication) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideThreadExecutor( /*JobExecutor jobExecutor*/): ThreadExecutor {
        return JobExecutor()
        //return jobExecutor;
    }

    @Provides
    @Singleton
    fun provideUIThread( /*UIThread uiThread*/): PostExecutionThread {
        return UIThread()
        //return uiThread;
    }

}