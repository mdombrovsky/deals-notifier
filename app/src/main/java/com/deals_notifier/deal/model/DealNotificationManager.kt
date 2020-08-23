package com.deals_notifier.deal.model

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.deals_notifier.R
import com.deals_notifier.post.model.Post

class DealNotificationManager(val context: Context) {

    private val dealChannelId: String = "deals"

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManagerCompat.createNotificationChannel(createDealChannel())
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDealChannel(): NotificationChannel {
        val channel = NotificationChannel(
            dealChannelId,
            "Deals",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "This will notify you of deals"
        return channel
    }

    fun sendDealNotification(post: Post) {
        val notification: Notification =
            NotificationCompat.Builder(context, dealChannelId)
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