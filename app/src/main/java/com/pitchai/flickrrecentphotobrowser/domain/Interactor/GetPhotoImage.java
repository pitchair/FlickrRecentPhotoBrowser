package com.pitchai.flickrrecentphotobrowser.domain.Interactor;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread;
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor;
import com.pitchai.flickrrecentphotobrowser.presentation.view.GridPhotoImageView;

import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by pitchairajamani on 3/13/17.
 */

public class GetPhotoImage extends UseCase<Bitmap, GetPhotoImage.Params> {

    Context context;

    public GetPhotoImage(Context context, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor,postExecutionThread);
        this.context = context;
    }

    @Override
    Observable<Bitmap> buildUseCaseObservable(final Params params) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(final ObservableEmitter<Bitmap> e) throws Exception {
                /*Glide.with(context).load("https://farm1.staticflickr.com/725/32773841554_a67f5a8f44.jpg")
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                e.onNext(resource);
                                e.onComplete();
                            }

                            @Override
                            public void onLoadFailed(Exception exception, Drawable errorDrawable) {
                                e.onError(exception);
                            }
                        });*/

                try {
                    Bitmap bitmap = Glide.
                            with(context).
                            load(params.url).
                            asBitmap().
                            centerCrop().
                            into(params.width,params.height).
                            get();
                    e.onNext(bitmap);
                    e.onComplete();
                } catch (final ExecutionException ex) {
                    e.onError(ex);
                } catch (final InterruptedException ex) {
                    e.onError(ex);
                }
            }
        });
    }

    public static final class Params {
        private String url;
        private int width;
        private int height;

        private Params(String url, int width, int height) {
            this.url = url;
            this.width = width;
            this.height = height;
        }

        public static Params forPhotoInfo(String url, int width, int height) {
            return new Params(url, width,height);
        }

    }
}
