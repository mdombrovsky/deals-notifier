package com.deals_notifier.main.controller

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.deals_notifier.deal.controller.DealFragmentController
import com.deals_notifier.deal.model.DealManager
import com.deals_notifier.deal.ui.DealFragment
import com.deals_notifier.main.ui.TabAdapter
import com.deals_notifier.query.controller.QueryFragmentController
import com.deals_notifier.query.ui.QueryFragment
import com.deals_notifier.settings.controller.SettingsFragmentController
import com.deals_notifier.settings.ui.SettingsFragment

class TabController(
    val activity: AppCompatActivity
) {
    val tabAdapter: TabAdapter = TabAdapter(this)
    private val context: Context = activity as Context

    private val dealFragmentController = DealFragmentController(context)

    private val settingsFragmentController =
        SettingsFragmentController(
            context = context,
            onModified = { onModified() })

    private val queryFragmentController =
        QueryFragmentController(
            context = context,
            onModified = { onModified() }
        )

    fun getDealFragment(): DealFragment {
        return dealFragmentController.dealFragment
    }

    fun getQueryFragment(): QueryFragment {
        return queryFragmentController.queryFragment
    }

    fun getSettingsFragment(): SettingsFragment {
        return settingsFragmentController.settingsFragment
    }

    private fun onModified() {
        DealManager.instance!!.reset()
        dealFragmentController.refresh()
    }
}