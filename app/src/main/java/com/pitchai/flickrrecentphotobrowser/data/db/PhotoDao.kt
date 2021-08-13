package com.pitchai.flickrrecentphotobrowser.data.db

import androidx.paging.PagingSource
import androidx.room.*
import com.pitchai.flickrrecentphotobrowser.data.model.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<Photo>)

    @Query(
        "SELECT * FROM photos"
    )
    fun photos(): PagingSource<Int, Photo>

    @SuppressWarnings
    @Query(
        "SELECT * FROM photos"
    )
    fun getAllPhotos(): Flow<List<Photo>>

    @Query(
        "SELECT * FROM photos WHERE id= :id"
    )
    fun getPhoto(id: String): Flow<Photo>

    @Query("DELETE FROM photos")
    suspend fun clearPhotos()
}