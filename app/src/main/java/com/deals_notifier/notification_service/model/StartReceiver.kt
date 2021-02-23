package com.deals_notifier.notification_service.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.deals_notifier.deal.model.DealManager
import com.deals_notifier.deal.model.DealService
import com.deals_notifier.deal.model.ValidDealHolder
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.scraper.model.ScraperHolder
import com.deals_notifier.settings.model.SettingsSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (intent.action == Intent.ACTION_BOOT_COMPLETED && SettingsSingleton.instance.notificationsEnabled && !DealService.isRunning()) {
                if (context != null) {

                    CoroutineScope(Dispatchers.IO).launch {

                        if (DealManager.instance == null) {
                            DealManager.initialize(
                                ValidDealHolder(
                                    QueryHolder.load(context),
                                    ScraperHolder.load(context)
                                )
                            )
                        }

                        DealService.start(context)
                    }
                }
            }
        }
    }
}