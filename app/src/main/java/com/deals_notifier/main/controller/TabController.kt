package com.deals_notifier.main.controller

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.deals_notifier.deal.controller.DealFragmentController
import com.deals_notifier.deal.ui.DealFragment
import com.deals_notifier.main.ui.TabAdapter
import com.deals_notifier.query.controller.QueryFragmentController
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.query.ui.QueryFragment

class TabController(
    val activity: AppCompatActivity,
    private val queryHolder: QueryHolder
) {
    val tabAdapter: TabAdapter = TabAdapter(this)
    private val context: Context = activity.applicationContext

    fun createDealFragment(): DealFragment {
        return DealFragmentController(queryHolder = queryHolder).dealFragment
    }

    fun createQueryFragment(): QueryFragment {
        return QueryFragmentController(queryHolder = queryHolder, context = context).queryFragment
    }
}