package com.pitchai.flickrrecentphotobrowser.domain

import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by pitchairajamani on 2/22/17.
 */
//@Singleton
class JobExecutor : ThreadExecutor {
    private val threadPoolExecutor: ThreadPoolExecutor
    override fun execute(runnable: Runnable) {
        threadPoolExecutor.execute(runnable)
    }

    private class JobThreadFactory : ThreadFactory {
        private var counter = 0
        override fun newThread(runnable: Runnable): Thread {
            return Thread(runnable, "android_" + counter++)
        }
    }

    //@Inject
    init {
        threadPoolExecutor = ThreadPoolExecutor(3, 5, 10, TimeUnit.SECONDS,
                LinkedBlockingQueue(), JobThreadFactory())
    }
}