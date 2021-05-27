/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pitchai.flickrrecentphotobrowser

import android.app.Application
import android.content.Context
import com.pitchai.flickrrecentphotobrowser.di.ApplicationComponent
import com.pitchai.flickrrecentphotobrowser.di.ApplicationModule
import com.pitchai.flickrrecentphotobrowser.di.DaggerApplicationComponent
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Android Main Application
 */
@InternalCoroutinesApi
class AndroidApplication : Application() {
    var applicationComponent: ApplicationComponent? = null
    override fun onCreate() {
        super.onCreate()
        initializeInjector()
    }

    private fun initializeInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    companion object {
        fun getAndroidApplication(context: Context?): AndroidApplication {
            return context?.applicationContext as AndroidApplication
        }
    }
}