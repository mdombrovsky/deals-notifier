package com.deals_notifier.deal.controller

import com.deals_notifier.deal.model.ValidDealHolder
import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.deal.ui.DealFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer

class DealFragmentController(private val validDealHolder: ValidDealHolder) {

    val dealFragment: DealFragment = DealFragment(this)

    private val dealController: DealController = DealController(validDealHolder)

    fun refresh(startRefresh: Boolean = true) {
        if (startRefresh) {
            dealFragment.swipeRefreshLayout.isRefreshing = true
        }
        CoroutineScope(IO).launch {
            dealController.refresh()
        }.invokeOnCompletion {
            dealFragment.swipeRefreshLayout.isRefreshing = false
        }
    }

    fun getDealAdapter(): DealAdapter {
        return dealController.dealAdapter
    }

    fun notifyViewCreated() {
        refresh()
    }
}