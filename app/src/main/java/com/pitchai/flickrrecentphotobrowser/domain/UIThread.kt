package com.pitchai.flickrrecentphotobrowser.domain

import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * MainThread (UI Thread) implementation based on a [Scheduler]
 * which will execute actions on the Android UI thread
 */
//@Singleton
class UIThread  // @Inject
    : PostExecutionThread {
    override val scheduler: Scheduler?
        get() = AndroidSchedulers.mainThread()
}