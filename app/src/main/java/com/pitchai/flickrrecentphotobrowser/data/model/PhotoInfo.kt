package com.pitchai.flickrrecentphotobrowser.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by pgrajama on 12/10/16.
 */
//Add to sealed class
data class PhotoListResponse(@SerializedName("photos") val photos: Photos)

data class Photos(
    @SerializedName("total") val total: Int = 0,
    @SerializedName("page") val page: Int = 0,
    @SerializedName("pages") val pages: Int = 0,
    @SerializedName("perpage") val perpage: Int = 0,
    @SerializedName("photo") val photoList: List<Photo> = emptyList(),
    val nextPage: Int? = null
)
