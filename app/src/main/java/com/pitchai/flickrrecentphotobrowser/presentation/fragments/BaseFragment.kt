package com.pitchai.flickrrecentphotobrowser.presentation.fragments

import android.app.Fragment
import android.os.Bundle
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.HasComponent

/**
 * Created by pitchairajamani on 4/24/17.
 */
open class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Gets a component for dependency injection by its type.
     */
    protected fun <C> getComponent(componentType: Class<C>): C {
        return componentType.cast((activity as HasComponent<C>).component)
    }
}