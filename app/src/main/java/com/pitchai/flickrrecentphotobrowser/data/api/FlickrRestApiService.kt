package com.pitchai.flickrrecentphotobrowser.data.api

import com.pitchai.flickrrecentphotobrowser.Constants
import com.pitchai.flickrrecentphotobrowser.data.model.PhotoListResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface FlickrRestApiService {
    /**
     * Get repos ordered by stars.
     */
    @GET("/services/rest")
    suspend fun getRecentPhotoList(
        @Query("method") method: String?,
        @Query("nojsoncallback") callback: String?,
        @Query("format") format: String?,
        @Query("api_key") apiKey: String?,
        @Query("page") requestPageNumber: Int,
        @Query("extras") extras: String?,
        @Query("per_page") perPage: Int = 50
    ): PhotoListResponse

    companion object {
        //private const val BASE_URL = "https://api.github.com/"

        fun create(): FlickrRestApiService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(Constants.END_POINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FlickrRestApiService::class.java)
        }
    }
}
