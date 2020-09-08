package com.deals_notifier.deal.model

import DealManager
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.deals_notifier.R
import com.deals_notifier.notification_service.model.DealNotificationManager
import com.deals_notifier.notification_service.model.DealNotificationManager.Companion.dealServiceChannelId
import com.deals_notifier.post.model.Post
import com.deals_notifier.post.model.SortedPostList
import com.deals_notifier.query.model.QueryHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.fixedRateTimer


class DealService : Service(), DealManager {

    companion object {
        fun start(context: Context, dealHolder: ValidDealHolder) {
            Companion.dealHolder = dealHolder
            ContextCompat.startForegroundService(
                context,
                Intent(context, DealService::class.java)
            )
        }

        fun isRunning(): Boolean {
            return dealManager != null
        }
        
        const val notificationId = 1

        var dealManager: DealManager? = null
            private set

        private var dealHolder: ValidDealHolder? = null
    }


    private var wakeLock: PowerManager.WakeLock? = null

    private lateinit var notificationManager: DealNotificationManager

    private lateinit var fixedRateTimer: Timer

    override val posts: SortedPostList
        get() = dealHolder!!.posts

    override val queryHolder: QueryHolder
        get() = dealHolder!!.queryHolder

    override fun reset() = dealHolder!!.reset()

    override suspend fun updatePosts(): List<Post> {
        //Keep track that it is called externally
        return dealHolder!!.updatePosts()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager =
            DealNotificationManager(
                this
            )
        startForeground(notificationId, generateNotification())
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            startDealService()
        }

        return START_STICKY
    }


    override fun stopService(name: Intent?): Boolean {
        stopDealService()
        return super.stopService(name)
    }

    private fun generateNotification(): Notification {
        return NotificationCompat.Builder(this, dealServiceChannelId)
            .setContentTitle("Deal Finder Service")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .build()
    }

    private fun startDealService() {
        if (isRunning()) {
            throw UnsupportedOperationException("Service already running")
        }
        dealManager = this

        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.javaClass.simpleName).apply {
                acquire()
            }
        }


        fixedRateTimer = fixedRateTimer("Get New Deals", false, 0, 10 * 1000) {
            CoroutineScope(IO).launch {
                if (dealHolder != null && this@DealService::notificationManager.isInitialized) {
                    if (!dealHolder!!.posts.isEmpty()) {
                        notificationManager.sendDealNotifications(dealHolder!!.updatePosts())
                    }

                }
            }
        }
    }

    private fun stopDealService() {
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            fixedRateTimer.cancel()
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, e.toString())
        } finally {
            dealHolder = null
            dealManager = null
        }
    }
}