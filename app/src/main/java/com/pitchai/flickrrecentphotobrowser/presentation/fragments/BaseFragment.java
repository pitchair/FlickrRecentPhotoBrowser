package com.pitchai.flickrrecentphotobrowser.presentation.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.pitchai.flickrrecentphotobrowser.AndroidApplication;
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.HasComponent;

/**
 * Created by pitchairajamani on 4/24/17.
 */

public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}
