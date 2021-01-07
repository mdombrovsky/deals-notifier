package com.deals_notifier.deal.controller

import android.content.Context
import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.deal.ui.DealFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DealFragmentController(
    private val context: Context
) {

    val dealFragment: DealFragment = DealFragment(this)

    private val dealController: DealController = DealController()


    fun refresh(startRefresh: Boolean = true) {
        if (startRefresh) {
            dealFragment.swipeRefreshLayout.isRefreshing = true
        }
        dealController.refresh() {
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