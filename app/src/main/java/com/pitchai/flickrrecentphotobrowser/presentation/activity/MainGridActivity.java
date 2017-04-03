package com.pitchai.flickrrecentphotobrowser.presentation.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.pitchai.flickrrecentphotobrowser.R;
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo;
import com.pitchai.flickrrecentphotobrowser.presentation.fragments.MainGridFragmentList;

import java.util.ArrayList;

import static com.pitchai.flickrrecentphotobrowser.Constants.CURRENT_POSITION;
import static com.pitchai.flickrrecentphotobrowser.Constants.PHOTO_BUNDLE;
import static com.pitchai.flickrrecentphotobrowser.Constants.PHOTO_LIST;

public class MainGridActivity extends FragmentActivity implements MainGridFragmentList
        .OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);
        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, MainGridFragmentList.newInstance());
        }
    }


    private void addFragment(int containerViewId, Fragment fragment) {
        final FragmentTransaction fragmentTransaction = this.getFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onPhotoSelect(ArrayList<Photo> photoList, int position) {

        final Intent i = new Intent(this, PhotoViewerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PHOTO_LIST, photoList);
        bundle.putInt(CURRENT_POSITION, position);
        i.putExtra(PHOTO_BUNDLE, bundle);
        startActivity(i);
       /* overridePendingTransition(R.animator.enter_activity_right_to_left, R.animator
                .exit_activity_left_to_right);*/
    }

}
