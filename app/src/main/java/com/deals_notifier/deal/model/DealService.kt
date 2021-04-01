package com.deals_notifier.deal.model

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.deals_notifier.R
import com.deals_notifier.notification_service.model.DealNotificationManager
import com.deals_notifier.notification_service.model.DealNotificationManager.Companion.dealServiceChannelId
import com.deals_notifier.notification_service.model.StartReceiver
import com.deals_notifier.post.model.Post
import com.deals_notifier.settings.model.SettingsSingleton
import com.deals_notifier.utility.PostRefreshListener
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlinx.coroutines.CoroutineScope as CoroutineScope1

/*
    Fetches Deals and Sends Notifications (even when application is closed)
 */
class DealService: Service(), DealServiceInterface {
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

    /* Timer keeping track of how long the service should be allowed to run in efficient mode */
    private var serviceTimer: Timer? = null

    /* power saving condition */
    private var powerSaving: Boolean = false

    private var alarmManager: AlarmManager? = null

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
        cancelAlarm()
        return super.stopService(name)
    }

    /* Creates a notification */
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

        if(powerSaving) {
            println("Running power saving deal service")
            startEfficientNotificationService()
        } else {
            println("Running regular deal service")
            startNotificationService()
        }
        startNotificationService()

        instance = this
    }


    @SuppressLint("WakelockTimeout")
    private fun startNotificationService() {
        startForeground(notificationId, generateNotification())

        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.javaClass.simpleName).apply {
                acquire()
            }
        }

        if (fixedRateTimer == null) {
            fixedRateTimer =
                createFixedRateTimerNotifications(SettingsSingleton.instance.notificationFrequencySeconds)
        }
    }

    private fun startEfficientNotificationService() {
        startAlarm()
        startForeground(notificationId, generateNotification())

        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.javaClass.simpleName).apply {
                acquire()
            }
        }
        generateNotificationTask()
        if (serviceTimer == null) {
            serviceTimer =
                createTimerEfficientService(300)
        }
    }

    /* creates a timer that executes generateNotificationTask once time is up */
    private fun createFixedRateTimerNotifications(secondsInterval: Int): Timer {
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

    /* Creates a timer to cancel the deal service after some time */
    private fun createTimerEfficientService(secondsInterval: Int): Timer {
        val serviceTimer = Timer()
        serviceTimer.schedule(object : TimerTask() {
            override fun run() {
                stopDealService()
            }
        }, secondsInterval * 1000.toLong())
        return serviceTimer
    }

    /* fetches the posts and sends the notifications */
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
        serviceTimer?.cancel()

        wakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }
        stopForeground(true)
    }

    override fun setNotificationFrequency(seconds: Int) {
        fixedRateTimer?.cancel()
        fixedRateTimer = createFixedRateTimerNotifications(seconds)
    }

    override fun setPowerSaving(enabled: Boolean) {
        this.powerSaving = enabled
    }

    private fun startAlarm() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, StartReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager?.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
            AlarmManager.INTERVAL_HALF_HOUR,
            pendingIntent
        )
    }

    private fun cancelAlarm() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, StartReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager?.cancel(pendingIntent)
    }
}