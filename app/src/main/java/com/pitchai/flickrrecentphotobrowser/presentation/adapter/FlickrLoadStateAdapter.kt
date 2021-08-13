
package com.pitchai.flickrrecentphotobrowser.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pitchai.flickrrecentphotobrowser.R
import com.pitchai.flickrrecentphotobrowser.databinding.FlickrLoadStateFooterViewItemBinding

class FlickrLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<FlickrLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: FlickrLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FlickrLoadStateViewHolder {
        return FlickrLoadStateViewHolder.create(parent, retry)
    }
}

class FlickrLoadStateViewHolder(
    private val binding: FlickrLoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): FlickrLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.flickr_load_state_footer_view_item, parent, false)
            val binding = FlickrLoadStateFooterViewItemBinding.bind(view)
            return FlickrLoadStateViewHolder(binding, retry)
        }
    }
}
