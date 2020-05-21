package com.pitchai.flickrrecentphotobrowser.domain.Interactor

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor
import io.reactivex.Observable
import java.util.concurrent.ExecutionException

/**
 * Created by pitchairajamani on 3/13/17.
 */
class GetPhotoImage(var context: Context, threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread) : UseCase<Bitmap, GetPhotoImage.Params>(threadExecutor, postExecutionThread) {
    public override fun buildUseCaseObservable(params: Params): Observable<Bitmap>? {
        return Observable.create { e -> /*Glide.with(context).load("https://farm1.staticflickr.com/725/32773841554_a67f5a8f44.jpg")
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
                val bitmap = Glide.with(context).load(params.url).asBitmap().centerCrop().into(params.width, params.height).get()
                e.onNext(bitmap)
                e.onComplete()
            } catch (ex: ExecutionException) {
                e.onError(ex)
            } catch (ex: InterruptedException) {
                e.onError(ex)
            }
        }
    }

    class Params private constructor(val url: String?, val width: Int, val height: Int) {

        companion object {
            fun forPhotoInfo(url: String?, width: Int, height: Int): Params {
                return Params(url, width, height)
            }
        }

    }

}