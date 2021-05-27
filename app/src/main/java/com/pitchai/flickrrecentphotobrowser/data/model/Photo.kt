package com.pitchai.flickrrecentphotobrowser.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by pgrajama on 12/10/16.
 */
@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey @field:SerializedName("id") val id: String,
    @field:SerializedName("owner") var owner: String,
    @field:SerializedName("secret") var secret: String,
    @field:SerializedName("server") var server: String,
    @field:SerializedName("farm") var farm: Int,
    @field:SerializedName("title") var title: String?,
    @field:SerializedName("isPublic") var isPublic: Int,
    @field:SerializedName("isFriend") var isFriend: Int,
    @field:SerializedName("isFamily") var isFamily: Int,
    @field:SerializedName("ownername") var ownername: String?,
    @field:SerializedName("datetaken") var datetaken: String?,
    ) : Serializable