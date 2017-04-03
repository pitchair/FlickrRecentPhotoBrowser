package com.pitchai.flickrrecentphotobrowser;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.pitchai.flickrrecentphotobrowser.data.RestApiService;
import com.pitchai.flickrrecentphotobrowser.domain.JobExecutor;
import com.pitchai.flickrrecentphotobrowser.domain.UIThread;
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread;
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by pitchairajamani on 2/22/17.
 */

public class ApplicationComponent {

    private static ApplicationComponent mApplicationComponentInstance;
    private PostExecutionThread mUiThread;
    private ThreadExecutor mJobExecutor;
    private RestApiService mRestApiService;

    public static ApplicationComponent getInstance(Context context) {
        if (mApplicationComponentInstance == null) {
            synchronized (ApplicationComponent.class) {
                if (mApplicationComponentInstance == null) {
                    mApplicationComponentInstance = new ApplicationComponent(context);
                }
            }
        }
        return mApplicationComponentInstance;
    }

    private ApplicationComponent(Context context) {
        mUiThread = new UIThread();
        mJobExecutor = new JobExecutor();
        mRestApiService = new Retrofit.Builder()
                .baseUrl(Constants.END_POINT)
                /*.(RestAdapter.LogLevel.FULL).setLog(new AndroidLog(ApplicationComponent
                        .class.getSimpleName()))*/
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestApiService.class);
    }

    public PostExecutionThread getUIThread() {
        return  mUiThread;
    }

    public ThreadExecutor getJobExecutor() {
        return mJobExecutor;
    }

    public RestApiService getRestApiService() {
        return mRestApiService;
    }
}
