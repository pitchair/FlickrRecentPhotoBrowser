package com.pitchai.flickrrecentphotobrowser.presentation.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pitchai.flickrrecentphotobrowser.R
import com.pitchai.flickrrecentphotobrowser.presentation.ViewModel.UiModel
import com.pitchai.flickrrecentphotobrowser.utils.Utils
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.concurrent.ExecutionException


class RecyclerPhotoListAdapter(
    private val mContext: Context,
    private val listener: PhotoClickListener
) : PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(UIMODEL_COMPARATOR) {

    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return (oldItem is UiModel.PhotoItem && newItem is UiModel.PhotoItem &&
                        oldItem.photo.id == newItem.photo.id)
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                oldItem == newItem
        }
    }

    var columnWidth: Int = 0

    /**
     * RecyclerPhotoHolder holds the ImageView, Textview and Progressbar
     */
    internal inner class RecyclerPhotoHolder(itemView: View, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView
        val progressBar: ProgressBar
        val errorDrawable: Drawable

        init {
            itemView.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
            errorDrawable = mContext.resources.getDrawable(R.drawable.error_icon)
            imageView = itemView.findViewById<View>(R.id.PhotoImage) as ImageView
            progressBar = itemView.findViewById<View>(R.id.progress_bar) as ProgressBar
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel?.let {
            when (uiModel) {
                is UiModel.PhotoItem -> {

                    val photo = uiModel.photo
                    val photoHolder = holder as? RecyclerPhotoListAdapter.RecyclerPhotoHolder
                    //Fetch the image Asynchronously with url
                    try {
                        photoHolder?.progressBar?.visibility = View.GONE
                        val options = RequestOptions()
                            .override(
                                columnWidth,
                                columnWidth
                            )
                            .error(photoHolder?.errorDrawable)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .dontAnimate()
                            .dontTransform()
                        photoHolder?.imageView?.let {
                            Glide.with(mContext).load(Utils.getUrl(photo)).apply(options)
                                .into(it)
                        }
                    } catch (e: ExecutionException) {
                        photoHolder?.progressBar?.visibility = View.GONE
                        photoHolder?.imageView?.setImageDrawable(photoHolder.errorDrawable)
                    }
                }
            }
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.photo_items, parent, false)
        itemView.layoutParams =
            AbsListView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, columnWidth)
        return RecyclerPhotoHolder(itemView) { position ->
            listener.onItemClick(position)
        }

    }
}

interface PhotoClickListener {
    fun onItemClick(position: Int)
}

