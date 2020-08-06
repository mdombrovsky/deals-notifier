package com.deals_notifier.main.controller

import com.deals_notifier.deal.controller.DealFragmentController
import com.deals_notifier.deal.ui.DealFragment
import com.deals_notifier.query.controller.QueryFragmentController
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.query.ui.QueryFragment

class TabController() {

    private val queryHolder = QueryHolder()

    fun createDealFragment(): DealFragment {
        val fragment = DealFragment()
        DealFragmentController(dealFragment = fragment, queryHolder = queryHolder)
        return fragment
    }

    fun createQueryFragment(): QueryFragment {
        val fragment = QueryFragment()
        QueryFragmentController(queryFragment = fragment, queryHolder = queryHolder)
        return fragment

    }
}