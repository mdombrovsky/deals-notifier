package com.deals_notifier.main.controller

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.deals_notifier.deal.controller.DealFragmentController
import com.deals_notifier.deal.model.ValidDealHolder
import com.deals_notifier.deal.ui.DealFragment
import com.deals_notifier.main.ui.TabAdapter
import com.deals_notifier.query.controller.QueryFragmentController
import com.deals_notifier.query.ui.QueryFragment

class TabController(
    val activity: AppCompatActivity,
    private val validDealHolder: ValidDealHolder
) {
    val tabAdapter: TabAdapter = TabAdapter(this)
    private val context: Context = activity.applicationContext

    private val dealFragmentController = DealFragmentController(validDealHolder, context)

    private val queryFragmentController =
        QueryFragmentController(
            queryHolder = validDealHolder.queryHolder,
            context = context,
            onModified = { onModified() }
        )

    fun getDealFragment(): DealFragment {
        return dealFragmentController.dealFragment
    }

    fun getQueryFragment(): QueryFragment {
        return queryFragmentController.queryFragment
    }

    private fun onModified() {
        validDealHolder.reset()
        dealFragmentController.refresh()
    }
}