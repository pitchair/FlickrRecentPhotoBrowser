package com.pitchai.flickrrecentphotobrowser.presentation.activity

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.support.v4.view.ViewPager.PageTransformer
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import com.pitchai.flickrrecentphotobrowser.Constants
import com.pitchai.flickrrecentphotobrowser.R
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo
import com.pitchai.flickrrecentphotobrowser.presentation.fragments.PhotoFragment
import java.util.*

class PhotoViewerActivity : BaseActivity(), PhotoFragment.OnFragmentInteractionListener, OnPageChangeListener {
    private var mAdapter: ImagePagerAdapter? = null
    private var mPager: ViewPager? = null
    private var mPhotoArrayList: ArrayList<Photo>? = null
    private var mCurrentPosition = 0
    private var mPhoto: Photo? = null
    private var mTextViewTitle: TextView? = null
    private var mTextViewBasicInfo: TextView? = null
    private var mTextViewHeader: TextView? = null
    private val mAlertDialog: AlertDialog? = null
    var mProgressBar: ProgressBar? = null
    var bundle: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        bundle = intent.getBundleExtra(Constants.PHOTO_BUNDLE)
        mPhotoArrayList = bundle?.getSerializable(Constants.PHOTO_LIST) as ArrayList<Photo>
        mCurrentPosition = bundle?.getInt(Constants.CURRENT_POSITION) ?: 0
        initializeViews()
        updateUI(bundle?.getInt(Constants.CURRENT_POSITION) ?: 0)
        // Set up activity to go full screen
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        toggleActionBar()
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    private fun initializeViews() {
        mProgressBar = findViewById<View>(R.id.photo_activity_progressbar) as ProgressBar
        mTextViewTitle = findViewById<View>(R.id.photo_title) as TextView
        mTextViewBasicInfo = findViewById<View>(R.id.photo_basic_info) as TextView
        mTextViewHeader = findViewById<View>(R.id.photo_number) as TextView
        mAdapter = ImagePagerAdapter(supportFragmentManager)
        mPager = findViewById<View>(R.id.view_pager) as ViewPager
        mPager?.adapter = mAdapter
        mPager?.pageMargin = resources.getDimension(R.dimen.activity_horizontal_margin).toInt()
        mPager?.offscreenPageLimit = 2
        mPager?.addOnPageChangeListener(this)
        mPager?.setPageTransformer(false, ZoomOutPageTransformer())
        if (mCurrentPosition != -1) {
            mPager?.currentItem = mCurrentPosition
        }
    }

    /**
     * When User Clicks the Image. Auto hide the actionbar
     * to make full screen for picture view
     */
    override fun onClickImageView() {
        val vis = mPager?.systemUiVisibility ?: 0
        if (vis and View.SYSTEM_UI_FLAG_LOW_PROFILE != 0) {
            mPager?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        } else {
            mPager?.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
        }
    }

    private fun updateUI(position: Int) {
        mPhoto = mPhotoArrayList?.get(position)
        mTextViewBasicInfo?.text = mPhoto?.ownername + ", " + mPhoto?.datetaken
        mTextViewTitle?.text = mPhoto?.title
        mTextViewHeader?.text = (position + 1).toString() + " of " + mPhotoArrayList?.size
    }

    /**
     * ImagePagerAdapter handles the user swipe to navigate the picture
     * using PhotoFragments
     */
    private inner class ImagePagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return mPhotoArrayList?.size ?: 0
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

        override fun getItem(position: Int): Fragment {
            return PhotoFragment.Companion.newInstance(mPhotoArrayList?.get(position))
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
    private fun toggleActionBar() {
        val actionBar = actionBar

        // Hide title text and set home as up
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)

        // Hide and show the ActionBar as the visibility changes
        mPager?.setOnSystemUiVisibilityChangeListener { vis ->
            if (vis and View.SYSTEM_UI_FLAG_LOW_PROFILE != 0) {
                //mTextView.setVisibility(View.INVISIBLE);
                actionBar.hide()
            } else {
                //mTextView.setVisibility(View.VISIBLE);
                actionBar.show()
            }
        }

        // Start low profile mode and hide ActionBar
        mPager?.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
        actionBar.hide()
    }

    /**
     * Class that implements zoom out page transformer
     */
    inner class ZoomOutPageTransformer : PageTransformer {
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