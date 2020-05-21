package com.pitchai.flickrrecentphotobrowser.presentation.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.pitchai.flickrrecentphotobrowser.R
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.Photo
import com.pitchai.flickrrecentphotobrowser.domain.Interactor.GetPhotoImage
import com.pitchai.flickrrecentphotobrowser.domain.executor.PostExecutionThread
import com.pitchai.flickrrecentphotobrowser.domain.executor.ThreadExecutor
import com.pitchai.flickrrecentphotobrowser.presentation.fragments.MainGridFragmentList
import com.pitchai.flickrrecentphotobrowser.presentation.presenter.GridPhotoImagePresenter
import com.pitchai.flickrrecentphotobrowser.presentation.view.GridPhotoImageView
import com.pitchai.flickrrecentphotobrowser.utils.Utils
import java.util.*

/**
 * Created by pgrajama on 12/9/16.
 */
class RecyclerPhotoAdapter(private val mContext: Context, jobExecutor: ThreadExecutor, uiThread: PostExecutionThread) : RecyclerView.Adapter<ViewHolder?>() {
    private val mPhotoList: MutableList<Photo?>? = ArrayList()
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val gridPhotoImagePresenter: GridPhotoImagePresenter = GridPhotoImagePresenter(mContext, jobExecutor, uiThread)

    /**
     * RecyclerPhotoHolder holds the ImageView, Textview and Progressbar
     */
    internal inner class RecyclerPhotoHolder(itemView: View) : ViewHolder(itemView) {
        val imageView: ImageView
        val progressBar: ProgressBar
        val errorDrawable: Drawable
        val mTextView: TextView

        init {
            errorDrawable = mContext.resources.getDrawable(R.drawable.error_icon)
            imageView = itemView.findViewById<View>(R.id.PhotoImage) as ImageView
            progressBar = itemView.findViewById<View>(R.id.progress_bar) as ProgressBar
            mTextView = itemView.findViewById<View>(R.id.grid_number) as TextView
        }
    }

    /**
     * LoadingViewHolder holds the Textview and Progressbar
     * when user request load more
     */
    internal class LoadingViewHolder(itemView: View) : ViewHolder(itemView) {
        var footerProgressBar: ProgressBar

        init {
            footerProgressBar = itemView.findViewById<View>(R.id.loading_progress) as ProgressBar
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        if (viewType == VIEW_TYPE_ITEM) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.photo_items, parent, false)
            itemView.layoutParams = AbsListView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    MainGridFragmentList.Companion.mColWidth)
            return RecyclerPhotoHolder(itemView)
        } else if (viewType == VIEW_TYPE_LOADING) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.footer_progress_layout, parent, false)
            return LoadingViewHolder(itemView)
        }
        return null
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder is RecyclerPhotoHolder) {
            val photo = mPhotoList?.get(position)
            val photoHolder = holder
            photoHolder.mTextView.text = (position + 1).toString()
            //Fetch the image Asynchronously with url using Picasso library
            gridPhotoImagePresenter.execute(object : GridPhotoImageView {
                override fun renderPhotoImageView(bitmap: Bitmap?) {
                    photoHolder.progressBar.visibility = View.GONE
                    photoHolder.imageView.setImageBitmap(bitmap)
                }

                override fun renderError(error: String?) {
                    photoHolder.progressBar.visibility = View.GONE
                    photoHolder.imageView.setImageDrawable(photoHolder.errorDrawable)
                    Log.d(TAG, "Error in fetching image: $error")
                }
            }, GetPhotoImage.Params.Companion.forPhotoInfo(Utils.getUrl(photo), MainGridFragmentList.Companion.mColWidth, MainGridFragmentList.Companion.mColWidth))
        } else {
            val loadingViewHolder = holder as LoadingViewHolder?
            loadingViewHolder?.footerProgressBar?.isIndeterminate = true
        }
    }

    /**
     * Notify the adapter once updated list
     * is received
     *
     * @PhotoList- Updated list from server
     */
    fun updateList(photoList: List<Photo?>?) {
        photoList?.takeUnless { it.isEmpty() }?.apply {
            mPhotoList?.addAll(this)
            notifyDataSetChanged()
        }
    }

    fun clear() {
        mPhotoList?.clear()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (mPhotoList!![position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return mPhotoList?.size ?: 0
    }

    companion object {
        private val TAG = RecyclerPhotoAdapter::class.java.simpleName
    }

}