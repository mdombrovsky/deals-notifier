package com.deals_notifier.settings.controller

import android.content.Context
import android.util.Log
import com.deals_notifier.deal.model.DealService
import com.deals_notifier.scraper.controller.ScraperController
import com.deals_notifier.scraper.ui.ScraperAdapter
import com.deals_notifier.settings.model.SettingsSingleton
import com.deals_notifier.settings.ui.SettingsFragment

class SettingsFragmentController(val context: Context, private val onModified: () -> Unit) {

    companion object {
        private fun convertFrequencyStringToSeconds(item: String): Int {
            val frequency: Int = item.takeWhile { it.isDigit() }.toInt()
            when {
                item.contains("Second") -> {
                    return frequency
                }
                item.contains("Minute") -> {
                    return frequency * 60
                }
                item.contains("Hour") -> {
                    return frequency * 60 * 60
                }
                item.contains("Day") -> {
                    return frequency * 60 * 60 * 24
                }
                else -> {
                    throw UnsupportedOperationException("Unknown Frequency")
                }
            }
        }
    }

    val settingsFragment = SettingsFragment(this)

    private val scraperController: ScraperController =
        ScraperController(context = context, onModified = { onModified() })

    fun setNotifications(enabled: Boolean) {
        SettingsSingleton.instance.notificationsEnabled = enabled

        if (SettingsSingleton.instance.notificationsEnabled && !DealService.isRunning()) {
            DealService.start(context)
        } else if (!SettingsSingleton.instance.notificationsEnabled && DealService.isRunning()) {
            DealService.instance!!.stopDealService()
        }
    }

    fun setFrequency(string: String) {
        SettingsSingleton.instance.notificationFrequencySeconds =
            convertFrequencyStringToSeconds(string)

        DealService.instance?.setNotificationFrequency(SettingsSingleton.instance.notificationFrequencySeconds)

        Log.d(
            this.javaClass.simpleName,
            "Selected: $string (${convertFrequencyStringToSeconds(string)})"
        )
    }

    fun frequencySpinnerEnabled(): Boolean {
        return SettingsSingleton.instance.notificationsEnabled
    }

    fun getScraperAdapter(): ScraperAdapter {
        return scraperController.scraperAdapter
    }
}