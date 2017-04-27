package com.pitchai.flickrrecentphotobrowser.data.Internal.di.modules;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.pitchai.flickrrecentphotobrowser.BuildConfig;
import com.pitchai.flickrrecentphotobrowser.Constants;
import com.pitchai.flickrrecentphotobrowser.data.FlickrRestApiService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pitchairajamani on 4/5/17.
 */
@Module
public class FlickrApiModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(60 * 1000, TimeUnit.MILLISECONDS);

        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRestAdapter(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient)
                .baseUrl(Constants.END_POINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build();
    }

    @Provides
    @Singleton
    public FlickrRestApiService provideFlickrRestApiService(Retrofit restAdapter) {
        return restAdapter.create(FlickrRestApiService.class);
    }

}
