package com.deals_notifier.deal.controller

import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.deal.ui.DealFragment
import com.deals_notifier.query.model.QueryHolder

class DealFragmentController(
    private val queryHolder: QueryHolder
) {

    val dealFragment: DealFragment = DealFragment(this)


    fun createDealAdapter(): DealAdapter {
        return DealController(queryHolder = queryHolder).dealAdapter
    }
}