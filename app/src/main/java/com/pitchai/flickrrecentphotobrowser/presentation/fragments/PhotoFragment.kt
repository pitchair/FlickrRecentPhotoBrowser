package com.pitchai.flickrrecentphotobrowser.presentation.fragments

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.request.target.Target
import com.pitchai.flickrrecentphotobrowser.AndroidApplication
import com.pitchai.flickrrecentphotobrowser.Constants
import com.pitchai.flickrrecentphotobrowser.R
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo
import com.pitchai.flickrrecentphotobrowser.domain.Interactor.GetPhotoImage
import com.pitchai.flickrrecentphotobrowser.presentation.internal.di.modules.PhotoFragmentModule
import com.pitchai.flickrrecentphotobrowser.presentation.presenter.GridPhotoImagePresenter
import com.pitchai.flickrrecentphotobrowser.presentation.view.GridPhotoImageView
import com.pitchai.flickrrecentphotobrowser.utils.Utils
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 *
 *
 * to handle interaction events.
 * Use the [PhotoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoFragment : Fragment() {
    private var mImageView: ImageView? = null
    private var mProgressBar: ProgressBar? = null
    private var mListener: OnFragmentInteractionListener? = null

    @Inject
    lateinit var gridPhotoImagePresenter: GridPhotoImagePresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        mImageView = view.findViewById<View>(R.id.imageView) as ImageView
        mImageView!!.setOnClickListener { mListener!!.onClickImageView() }
        mProgressBar = view.findViewById<View>(R.id.progressbar) as ProgressBar
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val photo = arguments?.getSerializable(Constants.PHOTO_BUNDLE) as Photo
        gridPhotoImagePresenter.execute(object : GridPhotoImageView {
            override fun renderPhotoImageView(bitmap: Bitmap?) {
                mProgressBar?.visibility = View.GONE
                mImageView?.setImageBitmap(bitmap)
            }

            override fun renderError(Error: String?) {
                mProgressBar?.visibility = View.GONE
            }
        }, GetPhotoImage.Params.Companion.forPhotoInfo(Utils.getUrl(photo), Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidApplication.Companion.getAndroidApplication(this.activity).applicationComponent
                ?.plus(PhotoFragmentModule(this))
                ?.inject(this)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mImageView!!.setImageDrawable(null)
        gridPhotoImagePresenter.destroy()
    }

    /**
     * Activity interaction interface
     */
    interface OnFragmentInteractionListener {
        fun onClickImageView()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PhotoFragment.
         */
        fun newInstance(photo: Photo?): PhotoFragment {
            val fragment = PhotoFragment()
            val bundle = Bundle()
            bundle.putSerializable(Constants.PHOTO_BUNDLE, photo)
            fragment.arguments = bundle
            return fragment
        }
    }
}