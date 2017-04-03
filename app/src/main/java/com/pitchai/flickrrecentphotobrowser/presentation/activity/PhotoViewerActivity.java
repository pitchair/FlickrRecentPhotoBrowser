package com.pitchai.flickrrecentphotobrowser.presentation.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pitchai.flickrrecentphotobrowser.Constants;
import com.pitchai.flickrrecentphotobrowser.R;
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo;
import com.pitchai.flickrrecentphotobrowser.presentation.fragments.PhotoFragment;

import java.util.ArrayList;


public class PhotoViewerActivity extends FragmentActivity implements PhotoFragment
        .OnFragmentInteractionListener, ViewPager.OnPageChangeListener {

    private static final String TAG = PhotoViewerActivity.class.getSimpleName();
    private ImagePagerAdapter mAdapter;
    private ViewPager mPager;
    private ArrayList<Photo> mPhotoArrayList = null;
    private int mCurrentPosition;
    private Photo mPhoto;
    private TextView mTextViewTitle;
    private TextView mTextViewBasicInfo;
    private TextView mTextViewHeader;
    private AlertDialog mAlertDialog;
    ProgressBar mProgressBar;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        bundle = getIntent().getBundleExtra(Constants.PHOTO_BUNDLE);
        mPhotoArrayList = (ArrayList<Photo>) bundle.getSerializable(Constants.PHOTO_LIST);
        mCurrentPosition = bundle.getInt(Constants.CURRENT_POSITION);
        initializeViews();
        updateUI(bundle.getInt(Constants.CURRENT_POSITION));
        // Set up activity to go full screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toggleActionBar();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void initializeViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.photo_activity_progressbar);
        mTextViewTitle = (TextView) findViewById(R.id.photo_title);
        mTextViewBasicInfo = (TextView) findViewById(R.id.photo_basic_info);
        mTextViewHeader = (TextView) findViewById(R.id.photo_number);
        mAdapter = new ImagePagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);
        mPager.setPageMargin((int) getResources().getDimension(R.dimen.activity_horizontal_margin));
        mPager.setOffscreenPageLimit(2);
        mPager.addOnPageChangeListener(this);
        mPager.setPageTransformer(false, new ZoomOutPageTransformer());
        if (mCurrentPosition != -1) {
            mPager.setCurrentItem(mCurrentPosition);
        }

    }

    /**
     * When User Clicks the Image. Auto hide the actionbar
     * to make full screen for picture view
     */
    @Override
    public void onClickImageView() {
        final int vis = mPager.getSystemUiVisibility();
        if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        }
    }


    private void updateUI(int position) {
        mPhoto = mPhotoArrayList.get(position);
        mTextViewBasicInfo.setText(mPhoto.getOwnername() + ", " + mPhoto.getDatetaken());
        mTextViewTitle.setText(mPhoto.getTitle());
        mTextViewHeader.setText((position + 1) + " of " + mPhotoArrayList.size());
    }

    /**
     * ImagePagerAdapter handles the user swipe to navigate the picture
     * using PhotoFragments
     */
    private class ImagePagerAdapter extends FragmentPagerAdapter {

        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mPhotoArrayList.size();
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoFragment.newInstance(mPhotoArrayList.get(position));
        }
    }

    /**
     * OnPageChangeListener callback
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * When user selects the particular page, fetch the picture
     * from flickr async
     * OnPageChangeListener callback
     */
    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "page position: " + position);
        updateUI(position);
    }

    /**
     * OnPageChangeListener callback
     */

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * This function helps in hide/show when user clicks the image
     */
    private void toggleActionBar() {
        final ActionBar actionBar = getActionBar();

        // Hide title text and set home as up
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Hide and show the ActionBar as the visibility changes
        mPager.setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int vis) {
                        if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
                            //mTextView.setVisibility(View.INVISIBLE);
                            actionBar.hide();
                        } else {
                            //mTextView.setVisibility(View.VISIBLE);
                            actionBar.show();
                        }
                    }
                });

        // Start low profile mode and hide ActionBar
        mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        actionBar.hide();
    }

    /**
     * Class that implements zoom out page transformer
     */
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

}
