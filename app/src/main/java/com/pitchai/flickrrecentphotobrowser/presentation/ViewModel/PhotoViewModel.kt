package com.pitchai.flickrrecentphotobrowser.presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.pitchai.flickrrecentphotobrowser.data.model.Photo
import kotlinx.coroutines.flow.*
import com.pitchai.flickrrecentphotobrowser.data.Result
import com.pitchai.flickrrecentphotobrowser.data.source.PhotoRepo

class PhotoViewModel(private val photoRepository: PhotoRepo) : ViewModel() {

    fun getAllPhotos(): Flow<Result<List<Photo>>>  {
         return photoRepository.getAllPhotos().shareIn(viewModelScope, SharingStarted.Eagerly)
    }

    fun getPhoto(id: String): Flow<Result<Photo>> {
        return photoRepository.getPhoto(id).shareIn(viewModelScope, SharingStarted.Eagerly)
    }

//    fun getPhotoList(): Flow<Result<Photo>> {
//        return photoRepository.getPhotoList()
//            .transform { result->
//                when (result) {
//                    is Result.Loading -> {
//                        emit(Result.Loading)
//                    }
//                    is Result.Error -> {
//                        emit(Result.Error(result.error))
//                    }
//                    is Result.Success -> {
//                        result.valueOrNull?.photos?.photoList?.forEach {
//                            emit(Result.Success(it))
//                        }
//                    }
//                }
//            }
//    }


}
