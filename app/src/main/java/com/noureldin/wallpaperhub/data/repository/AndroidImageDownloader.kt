package com.noureldin.wallpaperhub.data.repository

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.noureldin.wallpaperhub.domain.repository.Downloader
import java.io.File

class AndroidImageDownloader(
    context: Context
): Downloader {
    private val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    override fun downloadFile(url: String, fileName: String?) {
        try {
            val title = fileName ?: "New Image"
            val request = DownloadManager.Request(url.toUri())
                .setMimeType("image/*")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(title)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + title + ".jpg"
                )
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            downloadManager.enqueue(request)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}