package com.pitchai.flickrrecentphotobrowser

/**
 * Created by pgrajama on 12/11/16.
 */
object Constants {
    const val CURRENT_POSITION = "current_position"
    const val PHOTO_LIST = "photo_list"
    const val PHOTO_BUNDLE = "photo_bundle"

    //Network
    const val END_POINT = "https://api.flickr.com"
    const val METHOD_GET_RECENT = "flickr.photos.getRecent"
    const val METHOD_GET_PHOTO_INFO = "flickr.photos.getInfo"
    const val FORMAT = "json"
    const val API_KEY = "1b9a4597837b50c02816482d87d2b6f2"
    const val NO_CALL_BACK = "1"
    const val EXTRAS = "date_taken,owner_name"
}