package com.pitchai.flickrrecentphotobrowser.data

import com.pitchai.flickrrecentphotobrowser.data.photoinfo.PhotoDetails
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.PhotoInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by pgrajama on 12/10/16.
 */
/**
 * Rest API interface
 */
interface FlickrRestApiService {
    @GET("/services/rest")
    fun getRecentPhotoList(@Query("method") method: String?,
                           @Query("nojsoncallback") callback: String?,
                           @Query("format") format: String?,
                           @Query("api_key") apiKey: String?,
                           @Query("page") requestPageNumber: Int,
                           @Query("extras") extras: String?): Observable<PhotoInfo?>?

    @GET("/services/rest")
    fun getPhotoInfo(@Query("method") method: String?,
                     @Query("nojsoncallback") callback: String?,
                     @Query("format") format: String?,
                     @Query("api_key") apiKey: String?,
                     @Query("photo_id") photoID: String?): Observable<PhotoDetails?>?
}