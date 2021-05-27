package com.pitchai.flickrrecentphotobrowser.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import com.pitchai.flickrrecentphotobrowser.Constants
import com.pitchai.flickrrecentphotobrowser.data.db.PhotoDataBase
import com.pitchai.flickrrecentphotobrowser.data.db.RemoteKeys
import com.pitchai.flickrrecentphotobrowser.data.model.Photo
import com.pitchai.flickrrecentphotobrowser.data.api.FlickrRestApiService
import java.io.IOException
import java.lang.Exception

private const val STARTING_PAGE_INDEX = 1
@ExperimentalPagingApi
class PhotoRemoteMediator (val service: FlickrRestApiService, val photoDataBase: PhotoDataBase): RemoteMediator<Int, Photo>(){

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Photo>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }


        try {
            val apiResponse = service.getRecentPhotoList(Constants.METHOD_GET_RECENT, Constants.NO_CALL_BACK, Constants.FORMAT, Constants.API_KEY, page, Constants.EXTRAS)

            val photoList = apiResponse.photos.photoList
            val endOfPaginationReached = photoList.isEmpty()
            photoDataBase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    photoDataBase.remoteKeysDao().clearRemoteKeys()
                    photoDataBase.photoDao().clearPhotos()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = photoList.map {
                    RemoteKeys(photoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                photoDataBase.remoteKeysDao().insertAll(keys)
                photoDataBase.photoDao().insertAll(photoList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        } catch( exception: Exception) {
            return MediatorResult.Error(exception)
        }

    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Photo>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { photo ->
                // Get the remote keys of the last item retrieved
                photoDataBase.remoteKeysDao().remoteKeysPhotoId(photo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Photo>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { photo ->
                // Get the remote keys of the first items retrieved
                photoDataBase.remoteKeysDao().remoteKeysPhotoId(photo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Photo>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { photoId ->
                photoDataBase.remoteKeysDao().remoteKeysPhotoId(photoId)
            }
        }
    }
}