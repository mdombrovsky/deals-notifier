package com.deals_notifier.deal.controller

import com.deals_notifier.deal.model.ValidDealHolder
import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.deal.ui.DealFragment

class DealFragmentController(private val validDealHolder: ValidDealHolder) {

    val dealFragment: DealFragment = DealFragment(this)

    fun createDealAdapter(): DealAdapter {
        return DealController(validDealHolder).dealAdapter
    }
}