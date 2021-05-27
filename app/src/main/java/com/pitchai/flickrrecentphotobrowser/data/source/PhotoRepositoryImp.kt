package com.pitchai.flickrrecentphotobrowser.data.source

import androidx.paging.*
import com.pitchai.flickrrecentphotobrowser.Constants
import com.pitchai.flickrrecentphotobrowser.data.Result
import com.pitchai.flickrrecentphotobrowser.data.api.FlickrRestApiService
import com.pitchai.flickrrecentphotobrowser.data.db.PhotoDataBase
import com.pitchai.flickrrecentphotobrowser.data.model.Photo
import com.pitchai.flickrrecentphotobrowser.data.model.PhotoListResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class PhotoRepositoryImp(private val flickrRestApiService: FlickrRestApiService, private val photoDataBase: PhotoDataBase) : PhotoRepo {
    override fun fetchRecentPhotoList() : Flow<PagingData<Photo>> {

        val pagingSourceFactory = { photoDataBase.photoDao().photos() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            //TODO in API interface to page size
            config = PagingConfig(pageSize = 50, enablePlaceholders = false),
            remoteMediator = PhotoRemoteMediator(
                flickrRestApiService,
                photoDataBase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getPhotoList() : Flow<Result<PhotoListResponse>> = flow {
        emit(Result.Loading)
        try {
            val apiResponse = flickrRestApiService.getRecentPhotoList(
                Constants.METHOD_GET_RECENT,
                Constants.NO_CALL_BACK,
                Constants.FORMAT,
                Constants.API_KEY,
                1,
                Constants.EXTRAS
            )
            emit(Result.Success(apiResponse))
        } catch (e: Throwable) {
            emit(Result.Error(e))
        }
    }

    override fun getAllPhotos(): Flow<Result<List<Photo>>> = channelFlow  {
        send(Result.Loading)
        photoDataBase.photoDao().getAllPhotos()
            .catch { e ->
                send(Result.Error(e))
            }
            .collectLatest {
                send(Result.Success(it))
            }
    }

    override fun getPhoto(id: String): Flow<Result<Photo>> = channelFlow {
        send(Result.Loading)
        photoDataBase.photoDao().getPhoto(id)
            .catch { e ->
                send(Result.Error(e))
            }
            .collectLatest {
                send(Result.Success(it))
            }
    }

}