package com.deals_notifier.settings.controller

import android.content.Context
import com.deals_notifier.deal.model.DealService
import com.deals_notifier.settings.model.SettingsSingleton
import com.deals_notifier.settings.ui.SettingsFragment

class SettingsFragmentController(val context: Context) {
    val settingsFragment = SettingsFragment(this)

    fun setNotifications(enabled: Boolean) {
        SettingsSingleton.instance.notificationsEnabled = enabled

        if (SettingsSingleton.instance.notificationsEnabled && !DealService.isRunning()) {
            DealService.start(context)
        } else if (!SettingsSingleton.instance.notificationsEnabled && DealService.isRunning()) {
            DealService.instance!!.stopDealService()
        }
    }

}