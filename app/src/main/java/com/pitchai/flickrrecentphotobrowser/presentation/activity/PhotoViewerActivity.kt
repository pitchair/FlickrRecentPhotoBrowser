package com.pitchai.flickrrecentphotobrowser.presentation.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pitchai.flickrrecentphotobrowser.AndroidApplication
import com.pitchai.flickrrecentphotobrowser.Constants
import com.pitchai.flickrrecentphotobrowser.R
import com.pitchai.flickrrecentphotobrowser.data.Result
import com.pitchai.flickrrecentphotobrowser.data.model.Photo
import com.pitchai.flickrrecentphotobrowser.databinding.ActivityPhotoBinding
import com.pitchai.flickrrecentphotobrowser.di.PhotoViewerComponent
import com.pitchai.flickrrecentphotobrowser.di.PhotoViewerModule
import com.pitchai.flickrrecentphotobrowser.presentation.ViewModel.PhotoViewModel
import com.pitchai.flickrrecentphotobrowser.presentation.fragments.PhotoFragment
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
class PhotoViewerActivity : BaseActivity(), PhotoFragment.OnFragmentInteractionListener,
    ViewPager.OnPageChangeListener {

    @Inject
    lateinit var photoViewModel: PhotoViewModel

    private lateinit var binding: ActivityPhotoBinding
    private var mAdapter: ImagePagerAdapter? = null
    private var mPhotoArrayList = ArrayList<Photo>()
    var photoViewerComponent: PhotoViewerComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        photoViewerComponent =
            AndroidApplication.getAndroidApplication(this).applicationComponent?.plus(
                PhotoViewerModule(this)
            )
        photoViewerComponent?.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mCurrentPosition = intent.getIntExtra(Constants.CURRENT_POSITION, 0);
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoViewModel.getAllPhotos()
                    .collectLatest { result ->
                        when (result) {
                            is Result.Loading -> {
                                binding.photoActivityProgressbar.visibility = View.INVISIBLE
                            }
                            is Result.Error -> {
                                /*TODO show error*/
                                Log.d("", "Error : " + result.error.message)
                            }
                            is Result.Success -> {
                                mPhotoArrayList.addAll(result.value)
                                mAdapter?.notifyDataSetChanged()
                                if (mCurrentPosition != -1) {
                                    binding.viewPager.currentItem = mCurrentPosition
                                }
                            }
                        }
                    }
            }
        }
        initializeViews()
        // Set up activity to go full screen
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //toggleActionBar()
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    private fun initializeViews() {
        mAdapter = ImagePagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = mAdapter
        binding.viewPager.pageMargin =
            resources.getDimension(R.dimen.activity_horizontal_margin).toInt()
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.addOnPageChangeListener(this)
        binding.viewPager.setPageTransformer(false, ZoomOutPageTransformer())

    }

    /**
     * When User Clicks the Image. Auto hide the actionbar
     * to make full screen for picture view
     */
    override fun onClickImageView() {
        val vis = binding.viewPager.systemUiVisibility ?: 0
        if (vis and View.SYSTEM_UI_FLAG_LOW_PROFILE != 0) {
            binding.viewPager.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        } else {
            binding.viewPager.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
        }
    }

    private fun updateUI(position: Int) {
        val mPhoto = mPhotoArrayList.get(position)
        val basicInfoText = "${mPhoto.ownername} , + ${mPhoto.datetaken}"
        binding.photoBasicInfo.text = basicInfoText
        binding.photoTitle.text = mPhoto.title
        val positionText = "${(position + 1)} of + ${mPhotoArrayList.size}"
        binding.photoNumber.text = positionText
    }

    /**
     * ImagePagerAdapter handles the user swipe to navigate the picture
     * using PhotoFragments
     */
    private inner class ImagePagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
        override fun getCount(): Int {
            return mPhotoArrayList.size
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

        override fun getItem(position: Int): PhotoFragment {
            return PhotoFragment.newInstance(mPhotoArrayList[position].id)
        }
    }

    /**
     * OnPageChangeListener callback
     */
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    /**
     * When user selects the particular page, fetch the picture
     * from flickr async
     * OnPageChangeListener callback
     */
    override fun onPageSelected(position: Int) {
        Log.d(TAG, "page position: $position")
        updateUI(position)
    }

    /**
     * OnPageChangeListener callback
     */
    override fun onPageScrollStateChanged(state: Int) {}

    /**
     * This function helps in hide/show when user clicks the image
     */
    //TODO...Handle action bar
    private fun toggleActionBar() {
        val actionBar = actionBar

        // Hide title text and set home as up
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)

        // Hide and show the ActionBar as the visibility changes
        binding.viewPager.setOnSystemUiVisibilityChangeListener { vis ->
            if (vis and View.SYSTEM_UI_FLAG_LOW_PROFILE != 0) {
                //mTextView.setVisibility(View.INVISIBLE);
                actionBar.hide()
            } else {
                //mTextView.setVisibility(View.VISIBLE);
                actionBar.show()
            }
        }

        // Start low profile mode and hide ActionBar
        binding.viewPager.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
        actionBar.hide()
    }

    /**
     * Class that implements zoom out page transformer
     */
    inner class ZoomOutPageTransformer : ViewPager.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            val pageWidth = view.width
            val pageHeight = view.height
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.alpha = 0f
            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                val vertMargin = pageHeight * (1 - scaleFactor) / 2
                val horzMargin = pageWidth * (1 - scaleFactor) / 2
                if (position < 0) {
                    view.translationX = horzMargin - vertMargin / 2
                } else {
                    view.translationX = -horzMargin + vertMargin / 2
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor

                // Fade the page relative to its size.
                view.alpha = MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                        (1 - MIN_SCALE) * (1 - MIN_ALPHA)
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.alpha = 0f
            }
        }
    }

    companion object {
        private val TAG = PhotoViewerActivity::class.java.simpleName
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
    }
}