package com.deals_notifier.deal.controller

import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.deal.ui.DealFragment
import com.deals_notifier.query.model.QueryHolder

class DealFragmentController(
    private val dealFragment: DealFragment,
    private val queryHolder: QueryHolder
) {

    init {
        dealFragment.controller = this
    }

    fun createDealAdapter(): DealAdapter {
        val dealAdapter = DealAdapter()
        DealController(dealAdapter = dealAdapter, queryHolder = queryHolder)
        return dealAdapter
    }
}