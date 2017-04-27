package com.pitchai.flickrrecentphotobrowser.data;

import com.pitchai.flickrrecentphotobrowser.data.photoinfo.PhotoDetails;
import com.pitchai.flickrrecentphotobrowser.data.photoinfo.PhotoInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pgrajama on 12/10/16.
 */

/**
 * Rest API interface
 */
public interface FlickrRestApiService {
    @GET("/services/rest")
    Observable<PhotoInfo> getRecentPhotoList(@Query("method") String method,
                                             @Query("nojsoncallback") String callback,
                                             @Query("format") String format,
                                             @Query("api_key") String apiKey,
                                             @Query("page") int requestPageNumber,
                                             @Query("extras") String extras);

    @GET("/services/rest")
    Observable<PhotoDetails> getPhotoInfo(@Query("method") String method,
                                          @Query("nojsoncallback") String callback,
                                          @Query("format") String format,
                                          @Query("api_key") String apiKey,
                                          @Query("photo_id") String photoID);


}
