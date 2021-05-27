package com.pitchai.flickrrecentphotobrowser.presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.pitchai.flickrrecentphotobrowser.data.model.Photo
import com.pitchai.flickrrecentphotobrowser.data.source.PhotoRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PhotoListViewModel(private val photoRepository: PhotoRepo) : ViewModel() {
    fun getPhotoList(): Flow<PagingData<UiModel>> {
        // Loads and transformations before the cachedIn operation will be cached, so that
        // multiple observers get the same data. This is true either for simultaneous
        // observers, or e.g. an Activity re-subscribing after being recreated
        val result = photoRepository.fetchRecentPhotoList().cachedIn(viewModelScope)
        return result.map { pagingData ->
            pagingData.map {
                UiModel.PhotoItem(it)
            }
        }
    }
}


sealed class UiModel {
    data class PhotoItem(val photo: Photo) : UiModel()
}
