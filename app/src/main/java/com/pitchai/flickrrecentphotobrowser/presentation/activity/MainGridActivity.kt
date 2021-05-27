package com.pitchai.flickrrecentphotobrowser.presentation.activity

import android.content.Intent
import android.os.Bundle
import com.pitchai.flickrrecentphotobrowser.Constants
import com.pitchai.flickrrecentphotobrowser.R
import com.pitchai.flickrrecentphotobrowser.databinding.ActivityMainGridBinding
import com.pitchai.flickrrecentphotobrowser.presentation.fragments.MainGridFragmentList
import kotlinx.coroutines.InternalCoroutinesApi


@OptIn(InternalCoroutinesApi::class)
class MainGridActivity : BaseActivity(), MainGridFragmentList.OnFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main_grid)
        val binding = ActivityMainGridBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, MainGridFragmentList.newInstance())
        }
        binding.toolbar.setTitle(R.string.app_name)
        setSupportActionBar(binding.toolbar)

    }

    override fun onPhotoSelect(position: Int) {
        val i = Intent(this, PhotoViewerActivity::class.java)
        i.putExtra(Constants.CURRENT_POSITION, position)
        startActivity(i)
    }
}