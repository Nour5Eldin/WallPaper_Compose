package com.noureldin.wallpaperhub

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltAndroidApp
class WallPaperHubApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Check if the device is running on Android Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "download_channel_id",
                "Download Notifications",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifications for download progress"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showDownloadNotification() {
        val notificationManager = getSystemService(NotificationManager::class.java)

        // Create a notification builder
        val builder = NotificationCompat.Builder(this, "download_channel_id")
            .setSmallIcon(R.mipmap.ic_launcher) // Set your app icon here
            .setContentTitle("Download in Progress")
            .setContentText("Your download is underway.")
            .setPriority(NotificationCompat.PRIORITY_LOW)

        // Show the notification
        notificationManager.notify(1, builder.build())
    }

    fun startDownload(imageSize: Int) {
        // Show the notification when the download starts
        showDownloadNotification()

        // Simulate download process (replace with actual download logic)
        CoroutineScope(Dispatchers.IO).launch {
            for (progress in 0..100) {
                delay(100) // Simulate time taken for download
                updateDownloadNotification(progress)
            }
            withContext(Dispatchers.Main) {
                onDownloadComplete() // Call when download is complete
            }
        }
    }

    fun updateDownloadNotification(progress: Int) {
        val notificationManager = getSystemService(NotificationManager::class.java)

        // Create a notification builder
        val builder = NotificationCompat.Builder(this, "download_channel_id")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Download in Progress")
            .setContentText("Download is $progress% complete.")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setProgress(100, progress, false) // Set the progress

        // Show the notification
        notificationManager.notify(1, builder.build())
    }

    fun onDownloadComplete() {
        val notificationManager = getSystemService(NotificationManager::class.java)

        // Create a notification builder for completion
        val builder = NotificationCompat.Builder(this, "download_channel_id")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Download Complete")
            .setContentText("Your download has finished.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Dismiss the notification when tapped

        // Show the notification
        notificationManager.notify(1, builder.build())
    }
}