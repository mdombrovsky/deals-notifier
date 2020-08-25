package com.deals_notifier.deal.model

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.deals_notifier.R
import com.deals_notifier.deal.model.DealNotificationManager.Companion.dealServiceChannelId


class DealService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification: Notification =
            NotificationCompat.Builder(this, dealServiceChannelId)
                .setContentTitle("Deal Finder Service")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build()

        startForeground(1, notification)

        return START_STICKY
    }
}