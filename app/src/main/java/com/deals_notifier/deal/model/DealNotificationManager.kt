package com.deals_notifier.deal.model

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.deals_notifier.R
import com.deals_notifier.post.model.Post

class DealNotificationManager(val context: Context) {

    companion object {
        const val dealNotificationChannelId: String = "deal notification"
        const val dealServiceChannelId: String = "deal finder"
    }

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManagerCompat.createNotificationChannel(createDealNotificationChannel())
            notificationManagerCompat.createNotificationChannel(createDealFinderChannel())
        }


        ContextCompat.startForegroundService(context, Intent(context, DealService::class.java))
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDealNotificationChannel(): NotificationChannel {
        val channel = NotificationChannel(
            dealNotificationChannelId,
            "Deals",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "This will notify you of deals"
        return channel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDealFinderChannel(): NotificationChannel {
        val channel = NotificationChannel(
            dealServiceChannelId,
            "Deal Finder",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "This will actively search for deals"
        return channel
    }

    fun sendDealNotification(post: Post) {
        val notification: Notification =
            NotificationCompat.Builder(context, dealNotificationChannelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText(post.title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .build()
        notificationManagerCompat.notify(post.id.hashCode(), notification)
    }

    fun sendDealNotifications(posts: List<Post>) {
        for (post: Post in posts) {
            sendDealNotification(post)
        }
    }

}