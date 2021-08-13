package com.pitchai.flickrrecentphotobrowser.presentation.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pitchai.flickrrecentphotobrowser.Constants
import com.pitchai.flickrrecentphotobrowser.R
import com.pitchai.flickrrecentphotobrowser.data.Result
import com.pitchai.flickrrecentphotobrowser.databinding.FragmentPhotoBinding
import com.pitchai.flickrrecentphotobrowser.di.PhotoFragmentModule
import com.pitchai.flickrrecentphotobrowser.presentation.activity.PhotoViewerActivity
import com.pitchai.flickrrecentphotobrowser.presentation.ViewModel.PhotoViewModel
import com.pitchai.flickrrecentphotobrowser.utils.Utils
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException
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
@InternalCoroutinesApi
class PhotoFragment : Fragment() {
    @Inject
    lateinit var photoViewModel: PhotoViewModel

    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var binding:FragmentPhotoBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        binding.imageView.setOnClickListener { mListener?.onClickImageView() }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photoId = arguments?.getString(Constants.PHOTO_ID)
        lifecycleScope.launch {
            photoId?.let {
                photoViewModel.getPhoto(it)
                    .collectLatest { result ->
                        when(result) {
                            is Result.Loading -> {
                                binding.progressbar.visibility = View.VISIBLE
                            }
                            is Result.Error -> {
                                binding.progressbar.visibility = View.GONE
                                Log.d("","SomethingWrong: " + result.error.toString())
                                binding.imageView.setImageDrawable(requireActivity().getDrawable(R.drawable.error_icon))
                            }
                            is Result.Success -> {
                                binding.progressbar.visibility = View.GONE
                                    val options = RequestOptions()
                                        .override(
                                            Utils.calculateWidth(requireActivity()),
                                            Utils.calculateWidth(requireActivity())
                                        )
                                        .error(R.drawable.error_icon)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .priority(Priority.HIGH)
                                        .dontAnimate()
                                        .dontTransform()
                                    Glide.with(requireActivity()).load(Utils.getUrl(result.value)).apply(options)
                                        .into(binding.imageView)
                            }
                        }
                    }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val photoViewerComponent = (activity as PhotoViewerActivity).photoViewerComponent
        photoViewerComponent?.plus(PhotoFragmentModule(this))?.inject(this)
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
        binding.imageView.setImageDrawable(null)
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
        fun newInstance(photoId: String): PhotoFragment {
            val fragment = PhotoFragment()
            val bundle = Bundle()
            //Todo remove searializing photo..send id instead
            bundle.putString(Constants.PHOTO_ID, photoId)
            fragment.arguments = bundle
            return fragment
        }
    }
}