package com.pitchai.flickrrecentphotobrowser.data.photoinfo

import java.io.Serializable

/**
 * Created by pgrajama on 12/10/16.
 */
class Photo : Serializable {
    var id: String? = null
    var owner: String? = null
    var secret: String? = null
    var server: String? = null
    var farm = 0
    var title: String? = null
    var isPublic = 0
    var isFriend = 0
    var isFamily = 0
    var urls: Urls? = null
    var ownername: String? = null
    var datetaken: String? = null

}