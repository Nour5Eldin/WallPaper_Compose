package com.noureldin.wallpaperhub.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.noureldin.wallpaperhub.data.local.ImageDatabase
import com.noureldin.wallpaperhub.data.mapper.toDomainModel
import com.noureldin.wallpaperhub.data.mapper.toFavoriteImageEntity
import com.noureldin.wallpaperhub.data.paging.EditorialFeedRemoteMediator
import com.noureldin.wallpaperhub.data.paging.SearchPagingSource
import com.noureldin.wallpaperhub.data.remote.UnsplashApiServices
import com.noureldin.wallpaperhub.data.util.Constants.ITEMS_PER_PAGE
import com.noureldin.wallpaperhub.domain.model.UnsplashImage
import com.noureldin.wallpaperhub.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ImageRepositoryImpl(
    private val unsplashApi: UnsplashApiServices,
    private val database: ImageDatabase
): ImageRepository {
    private val favoritesImageDao = database.favoriteImageDao()
    private val editorialFeedDao = database.editorialFeedDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getEditorialFeedImages(): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            remoteMediator = EditorialFeedRemoteMediator(unsplashApi, database),
            pagingSourceFactory = { editorialFeedDao.getAllEditorialFeedImages() }
        )
            .flow
            .map { pagingData ->
                pagingData.map { it.toDomainModel() }
            }
    }

    override suspend fun getImage(imageId: String): UnsplashImage {
        return unsplashApi.getImage(imageId).toDomainModel()
    }

    override fun searchImages(query: String): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {SearchPagingSource(query, unsplashApi)}
        ).flow
    }

    override fun getAllFavoriteImages(): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = { favoritesImageDao.getAllFavoriteImages() }
        )
            .flow
            .map { pagingData ->
                pagingData.map { it.toDomainModel() }
            }
    }

    override suspend fun toggleFavoriteStatus(image: UnsplashImage) {
        val isFavorite = favoritesImageDao.isImageFavorite(image.id)
        val favoriteImage = image.toFavoriteImageEntity()
        if (isFavorite){
            favoritesImageDao.deleteFavoriteImage(favoriteImage)
        }else {
            favoritesImageDao.insertFavoriteImage(favoriteImage)
        }
    }

    override fun getFavoriteImageIds(): Flow<List<String>> {
        return favoritesImageDao.getFavoriteImageIds()
    }
}