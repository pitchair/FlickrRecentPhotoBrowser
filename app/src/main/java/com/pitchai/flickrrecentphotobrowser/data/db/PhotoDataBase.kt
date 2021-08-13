package com.pitchai.flickrrecentphotobrowser.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pitchai.flickrrecentphotobrowser.data.model.Photo

@Database(
    entities = [Photo::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class PhotoDataBase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    companion object {
        @Volatile
        private var INSTANCE: PhotoDataBase? = null

        fun getInstance(context: Context): PhotoDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PhotoDataBase::class.java, "Photo.db"
            ).build()
    }
}