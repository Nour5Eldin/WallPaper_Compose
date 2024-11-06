package com.noureldin.wallpaperhub.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.noureldin.wallpaperhub.data.local.ImageDatabase
import com.noureldin.wallpaperhub.data.remote.UnsplashApiServices
import com.noureldin.wallpaperhub.data.repository.AndroidImageDownloader
import com.noureldin.wallpaperhub.data.repository.ImageRepositoryImpl
import com.noureldin.wallpaperhub.data.repository.NetworkConnectivityObserverImpl
import com.noureldin.wallpaperhub.data.util.Constants
import com.noureldin.wallpaperhub.data.util.Constants.IMAGE_WALLPAPER_DATABASE
import com.noureldin.wallpaperhub.domain.repository.Downloader
import com.noureldin.wallpaperhub.domain.repository.ImageRepository
import com.noureldin.wallpaperhub.domain.repository.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUnsplashApiService(): UnsplashApiServices {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        val retrofit = Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl(Constants.BASE_URL)
            .build()
        return retrofit.create(UnsplashApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideImageVistaDatabase(
        @ApplicationContext context: Context
    ): ImageDatabase {
        return Room
            .databaseBuilder(
                context,
                ImageDatabase::class.java,
                IMAGE_WALLPAPER_DATABASE
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideImageRepository(
        apiService: UnsplashApiServices,
        database: ImageDatabase
    ): ImageRepository {
        return ImageRepositoryImpl(apiService, database)
    }

    @Provides
    @Singleton
    fun provideAndroidImageDownloader(
        @ApplicationContext context: Context
    ): Downloader {
        return AndroidImageDownloader(context)
    }

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): NetworkConnectivityObserver {
        return NetworkConnectivityObserverImpl(context, scope)
    }


}