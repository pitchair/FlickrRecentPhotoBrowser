package com.pitchai.flickrrecentphotobrowser.presentation.adapter;

/**
 * Created by pgrajama on 12/9/16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pitchai.flickrrecentphotobrowser.R;
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo;
import com.pitchai.flickrrecentphotobrowser.domain.Interactor.GetPhotoImage;
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread;
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor;
import com.pitchai.flickrrecentphotobrowser.presentation.presenter.GridPhotoImagePresenter;
import com.pitchai.flickrrecentphotobrowser.presentation.view.GridPhotoImageView;
import com.pitchai.flickrrecentphotobrowser.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.pitchai.flickrrecentphotobrowser.presentation.fragments.MainGridFragmentList
        .mColWidth;


public class RecyclerPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = RecyclerPhotoAdapter.class.getSimpleName();
    private Context mContext;
    private List<Photo> mPhotoList = new ArrayList<>();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private GridPhotoImagePresenter gridPhotoImagePresenter;

    public RecyclerPhotoAdapter(Context context, ThreadExecutor jobExecutor, PostExecutionThread
            uiThread) {
        mContext = context;
        gridPhotoImagePresenter = new GridPhotoImagePresenter(mContext, jobExecutor, uiThread);
    }

    /**
     * RecyclerPhotoHolder holds the ImageView, Textview and Progressbar
     */
    class RecyclerPhotoHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private ProgressBar progressBar;
        private Drawable errorDrawable;
        private TextView mTextView;

        private RecyclerPhotoHolder(View itemView) {
            super(itemView);
            errorDrawable = mContext.getResources().getDrawable(R.drawable.error_icon);
            imageView = (ImageView) itemView.findViewById(R.id.PhotoImage);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            mTextView = (TextView) itemView.findViewById(R.id.grid_number);
        }

    }

    /**
     * LoadingViewHolder holds the Textview and Progressbar
     * when user request load more
     */
    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar footerProgressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            footerProgressBar = (ProgressBar) itemView.findViewById(R.id.loading_progress);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                    .photo_items, parent, false);
            itemView.setLayoutParams(new AbsListView.LayoutParams(RecyclerView.LayoutParams
                    .MATCH_PARENT,
                    mColWidth));
            return new RecyclerPhotoHolder(itemView);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                    .footer_progress_layout, parent, false);
            return new LoadingViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof RecyclerPhotoHolder) {

            Photo photo = mPhotoList.get(position);
            final RecyclerPhotoHolder photoHolder = (RecyclerPhotoHolder) holder;

            photoHolder.mTextView.setText(new Integer(position + 1).toString());
            //Fetch the image Asynchronously with url using Picasso library
            gridPhotoImagePresenter.execute(new GridPhotoImageView() {
                @Override
                public void renderPhotoImageView(Bitmap bitmap) {
                    photoHolder.progressBar.setVisibility(View.GONE);
                    photoHolder.imageView.setImageBitmap(bitmap);
                }

                @Override
                public void renderError(String error) {
                    photoHolder.progressBar.setVisibility(View.GONE);
                    photoHolder.imageView.setImageDrawable(photoHolder.errorDrawable);
                    Log.d(TAG, "Error in fetching image: " + error);
                }
            }, GetPhotoImage.Params.forPhotoInfo(Utils.getUrl(photo), mColWidth, mColWidth));

        } else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.footerProgressBar.setIndeterminate(true);
        }
    }

    /**
     * Notify the adapter once updated list
     * is received
     *
     * @PhotoList- Updated list from server
     */

    public void updateList(List<Photo> photoList) {
        mPhotoList.addAll(photoList);
        notifyDataSetChanged();
    }

    public void clear() {
        mPhotoList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mPhotoList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        if (mPhotoList == null) {
            return 0;
        }
        return mPhotoList.size();
    }
}
