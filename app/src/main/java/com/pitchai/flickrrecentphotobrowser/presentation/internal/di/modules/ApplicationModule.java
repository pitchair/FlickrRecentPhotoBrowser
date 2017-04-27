package com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules;

import android.content.Context;

import com.pitchai.flickrrecentphotobrowser.AndroidApplication;
import com.pitchai.flickrrecentphotobrowser.domain.JobExecutor;
import com.pitchai.flickrrecentphotobrowser.domain.UIThread;
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread;
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pitchairajamani on 4/4/17.
 */
@Module
public class ApplicationModule {
    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(/*JobExecutor jobExecutor*/) {
        return new JobExecutor();
        //return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread provideUIThread(/*UIThread uiThread*/) {
        return new UIThread();
        //return uiThread;
    }
}
