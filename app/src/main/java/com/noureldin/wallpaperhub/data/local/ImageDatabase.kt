package com.noureldin.wallpaperhub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.noureldin.wallpaperhub.data.local.entity.FavoriteImageEntity
import com.noureldin.wallpaperhub.data.local.entity.UnsplashImageEntity
import com.noureldin.wallpaperhub.data.local.entity.UnsplashRemoteKeys

@Database(entities = [FavoriteImageEntity::class, UnsplashImageEntity::class, UnsplashRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class ImageDatabase: RoomDatabase() {
    abstract fun favoriteImageDao(): FavoriteImagesDao
    abstract fun editorialFeedDao(): EditorialFeedDao
}