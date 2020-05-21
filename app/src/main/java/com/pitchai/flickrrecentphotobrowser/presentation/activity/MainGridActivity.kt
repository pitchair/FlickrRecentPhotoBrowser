package com.pitchai.flickrrecentphotobrowser.presentation.activity

import android.content.Intent
import android.os.Bundle
import com.pitchai.flickrrecentphotobrowser.Constants
import com.pitchai.flickrrecentphotobrowser.R
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo
import com.pitchai.flickrrecentphotobrowser.presentation.fragments.MainGridFragmentList
import java.util.*

class MainGridActivity : BaseActivity(), MainGridFragmentList.OnFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_grid)
        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, MainGridFragmentList.newInstance())
        }
    }

    override fun onPhotoSelect(photoList: ArrayList<Photo?>?, position: Int) {
        val i = Intent(this, PhotoViewerActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(Constants.PHOTO_LIST, photoList)
        bundle.putInt(Constants.CURRENT_POSITION, position)
        i.putExtra(Constants.PHOTO_BUNDLE, bundle)
        startActivity(i)
        /* overridePendingTransition(R.animator.enter_activity_right_to_left, R.animator
                .exit_activity_left_to_right);*/
    }
}