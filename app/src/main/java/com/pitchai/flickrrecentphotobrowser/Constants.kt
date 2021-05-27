package com.pitchai.flickrrecentphotobrowser

/**
 * Created by pgrajama on 12/11/16.
 */
object Constants {
    const val CURRENT_POSITION = "current_position"
    const val PHOTO_ID = "photo_id"

    //Network
    const val END_POINT = "https://api.flickr.com"
    const val METHOD_GET_RECENT = "flickr.photos.getRecent"
    const val FORMAT = "json"
    const val API_KEY = "" //Please provide your Flickr API key
    const val NO_CALL_BACK = "1"
    const val EXTRAS = "date_taken,owner_name"
}