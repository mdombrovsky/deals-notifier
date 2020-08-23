package com.deals_notifier.deal.controller

import android.content.Context
import com.deals_notifier.deal.model.DealNotificationManager
import com.deals_notifier.deal.model.ValidDealHolder
import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.deal.ui.DealFragment
import com.deals_notifier.post.model.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer

class DealFragmentController(
    private val validDealHolder: ValidDealHolder,
    private val context: Context
) {

    val dealFragment: DealFragment = DealFragment(this)

    private val dealController: DealController = DealController(validDealHolder)

    private val dealNotificationManager = DealNotificationManager(context)

    fun refresh(startRefresh: Boolean = true) {
        if (startRefresh) {
            dealFragment.swipeRefreshLayout.isRefreshing = true
        }
        var newPosts: List<Post> = ArrayList<Post>()
        CoroutineScope(IO).launch {
            newPosts = dealController.refresh()
        }.invokeOnCompletion {
            dealFragment.swipeRefreshLayout.isRefreshing = false
            dealNotificationManager.sendDealNotifications(newPosts)
        }
    }

    fun getDealAdapter(): DealAdapter {
        return dealController.dealAdapter
    }

    fun notifyViewCreated() {
        refresh()
    }
}