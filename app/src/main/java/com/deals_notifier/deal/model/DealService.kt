package com.deals_notifier.deal.model

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
import com.deals_notifier.settings.model.SettingsSingleton
import com.deals_notifier.utility.PostRefreshListener
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlinx.coroutines.CoroutineScope as CoroutineScope1


class DealService : Service(), DealServiceInterface {

    companion object {
        fun start(context: Context) {
            ContextCompat.startForegroundService(
                context,
                Intent(context, DealService::class.java)
            )
        }

        fun isRunning(): Boolean {
            return instance != null
        }

        const val notificationId = 1

        var instance: DealServiceInterface? = null
            private set

    }


    private var wakeLock: PowerManager.WakeLock? = null

    private var fixedRateTimer: Timer? = null

    private var notificationRunning: Boolean = false

    private lateinit var notificationManager: DealNotificationManager


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager =
            DealNotificationManager(
                this
            )
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            startThisService()
        }

        return START_STICKY
    }


    override fun stopService(name: Intent?): Boolean {
        stopThisService()
        return super.stopService(name)
    }

    private fun generateNotification(): Notification {
        return NotificationCompat.Builder(this, dealServiceChannelId)
            .setContentTitle("Deal Finder Service")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .build()
    }

    private fun startThisService() {
        if (isRunning()) {
            throw UnsupportedOperationException("Service already running")
        }
        startNotificationService()

        instance = this
    }

    private fun stopThisService() {
        try {
            cancelNotificationService()
            stopSelf()
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, e.toString())
        } finally {
            instance = null
        }
    }

    private fun cancelNotificationService() {
        fixedRateTimer?.cancel()

        wakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }
        stopForeground(true)

    }

    private fun startNotificationService() {

        startForeground(notificationId, generateNotification())

        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.javaClass.simpleName).apply {
                acquire()
            }
        }


        if (fixedRateTimer == null) {
            fixedRateTimer =
                createFixedRateTimer(SettingsSingleton.instance.notificationFrequencySeconds)
        }
    }

    private fun createFixedRateTimer(secondsInterval: Int): Timer {
        return (
                fixedRateTimer(
                    "Get New Deals",
                    false,
                    0,
                    secondsInterval * 1000.toLong()
                ) {
                    generateNotificationTask()
                }
                )
    }

    private fun generateNotificationTask() {
        CoroutineScope1(context = IO).launch {
            Log.d(this.javaClass.simpleName, "Notification task executing")

            if (DealManager.instance != null && this@DealService::notificationManager.isInitialized) {
                if (!DealManager.instance?.posts?.isEmpty()!!) {

                    DealManager.instance!!.updatePosts(object : PostRefreshListener {
                        override fun onComplete(data: List<Post>) {
                            CoroutineScope1(IO).launch {
                                notificationManager.sendDealNotifications(data)
                            }
                        }
                    })
                }
            }
        }
    }

    override fun stopDealService() {
        stopThisService()
    }

    override fun setNotificationFrequency(seconds: Int) {
        fixedRateTimer?.cancel()
        fixedRateTimer = createFixedRateTimer(seconds)
    }


}