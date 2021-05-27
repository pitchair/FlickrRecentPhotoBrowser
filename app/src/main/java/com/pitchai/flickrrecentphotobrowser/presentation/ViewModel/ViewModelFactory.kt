
package com.pitchai.flickrrecentphotobrowser.presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pitchai.flickrrecentphotobrowser.data.source.PhotoRepo
import javax.inject.Inject

/**
 * Factory for ViewModels
 */
class ViewModelFactory @Inject constructor(var repository: PhotoRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotoListViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
