package com.pitchai.flickrrecentphotobrowser.data.source

import androidx.paging.PagingData
import com.pitchai.flickrrecentphotobrowser.data.Result
import com.pitchai.flickrrecentphotobrowser.data.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepo {
    fun fetchRecentPhotoList() : Flow<PagingData<Photo>>
    fun getAllPhotos(): Flow<Result<List<Photo>>>
    fun getPhoto(id: String): Flow<Result<Photo>>
}