package com.deals_notifier.settings.model

class SettingsSingleton private constructor() {

    companion object {
        val instance: SettingsSingleton by lazy { HOLDER.INSTANCE }
    }

    private object HOLDER {
        val INSTANCE = SettingsSingleton()
    }

    var notificationsEnabled: Boolean = false
    var powerSavingEnabled: Boolean = false
    var notificationFrequencySeconds: Int = 0

}