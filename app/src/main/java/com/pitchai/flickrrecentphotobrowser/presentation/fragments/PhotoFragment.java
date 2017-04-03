package com.pitchai.flickrrecentphotobrowser.presentation.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.request.target.Target;
import com.pitchai.flickrrecentphotobrowser.ApplicationComponent;
import com.pitchai.flickrrecentphotobrowser.R;
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo;
import com.pitchai.flickrrecentphotobrowser.domain.Interactor.GetPhotoImage;
import com.pitchai.flickrrecentphotobrowser.presentation.presenter.GridPhotoImagePresenter;
import com.pitchai.flickrrecentphotobrowser.presentation.view.GridPhotoImageView;
import com.pitchai.flickrrecentphotobrowser.utils.Utils;

import static com.pitchai.flickrrecentphotobrowser.Constants.PHOTO_BUNDLE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment {
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private OnFragmentInteractionListener mListener;
    private GridPhotoImagePresenter gridPhotoImagePresenter;

    public PhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PhotoFragment.
     */
    public static PhotoFragment newInstance(Photo photo) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PHOTO_BUNDLE, photo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        gridPhotoImagePresenter = new GridPhotoImagePresenter(getActivity(),
                getApplicationComponent().getJobExecutor(), getApplicationComponent().getUIThread
                ());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        mImageView = (ImageView) view.findViewById(R.id.imageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickImageView();
            }
        });
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Photo photo = (Photo) getArguments().getSerializable(PHOTO_BUNDLE);
        gridPhotoImagePresenter.execute(new GridPhotoImageView() {
            @Override
            public void renderPhotoImageView(Bitmap bitmap) {
                mProgressBar.setVisibility(View.GONE);
                mImageView.setImageBitmap(bitmap);
            }

            @Override
            public void renderError(String Error) {
                mProgressBar.setVisibility(View.GONE);
            }
        }, GetPhotoImage.Params.forPhotoInfo(Utils.getUrl(photo), Target.SIZE_ORIGINAL, Target
                .SIZE_ORIGINAL));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PhotoFragment.OnFragmentInteractionListener) {
            mListener = (PhotoFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onDestroy() {
        super.onDestroy();
        mImageView.setImageDrawable(null);
        gridPhotoImagePresenter.destroy();
    }

    /**
     * Activity interaction interface
     */
    public interface OnFragmentInteractionListener {
        void onClickImageView();
    }

    private ApplicationComponent getApplicationComponent() {
        return ApplicationComponent.getInstance(getActivity());
    }
}
