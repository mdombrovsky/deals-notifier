package com.deals_notifier.settings.model

import android.content.Context
import android.util.Log
import com.deals_notifier.MainActivity
import com.deals_notifier.main.controller.TabController
import com.deals_notifier.query.model.Query
import com.deals_notifier.query.model.QueryHolder
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class SettingsSingleton private constructor() {
    companion object {
        val instance: SettingsSingleton by lazy { HOLDER.INSTANCE }
        val filename: String = "settings.json"
    }

    private object HOLDER {
        val INSTANCE = SettingsSingleton()
    }

    var notificationsEnabled: Boolean = false
    var powerSavingEnabled: Boolean = false
    var notificationFrequencySeconds: Int = 0
    var darkModeEnabled: Boolean = false

    fun load(context: Context) {
        val file = File(context.filesDir, SettingsSingleton.filename)
        try {
            parseSettingsJSON(JSONObject(file.readText()))
        } catch (e: Exception) {
            Log.e(TabController::class.simpleName, "Error reading queries from file: $e")
        }
    }

    fun parseSettingsJSON(settingsJSON:JSONObject) {
        powerSavingEnabled =  settingsJSON.get("powerSaving").toString().toBoolean();
        notificationsEnabled = settingsJSON.get("notifications").toString().toBoolean();
        notificationFrequencySeconds = settingsJSON.get("notificationFrequency").toString().toInt();
        darkModeEnabled = settingsJSON.get("darkMode").toString().toBoolean();
    }

     fun save(context: Context) {
        val file = File(context.filesDir, SettingsSingleton.filename)
        try {
            file.writeText(this.toJSON().toString(4))
            Log.d(
                this.javaClass.simpleName,
                "Successfully saved queries to file: $file"
            )
        } catch (e: java.lang.Exception) {
            Log.e(this.javaClass.simpleName, "Error writing queries to file: $e")
        }
    }


    fun toJSON(): JSONObject {
        val settingsJSON = JSONObject()
        settingsJSON.put("powerSaving", powerSavingEnabled.toString());
        settingsJSON.put("notifications", notificationsEnabled.toString());
        settingsJSON.put("notificationFrequency", notificationFrequencySeconds.toString());
        settingsJSON.put("darkMode", darkModeEnabled.toString());
        return settingsJSON
    }


}
