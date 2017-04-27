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
package com.pitchai.flickrrecentphotobrowser;

import android.app.Application;
import android.content.Context;

import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.component.ApplicationComponent;
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.component
        .DaggerApplicationComponent;
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules.ApplicationModule;


/**
 * Android Main Application
 */
public class AndroidApplication extends Application {
  ApplicationComponent mApplicationComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    initializeInjector();
  }

  private void initializeInjector() {
    this.mApplicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
  }

  public static AndroidApplication getAndroidApplication(Context context) {
    return (AndroidApplication) context.getApplicationContext();
  }

  public ApplicationComponent getApplicationComponent() {
      return mApplicationComponent;
  }
}
