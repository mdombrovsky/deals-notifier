package com.deals_notifier.deal.controller

import com.deals_notifier.deal.model.DealManager
import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.post.model.Post
import com.deals_notifier.utility.PostRefreshListener

class DealController() {

    val dealAdapter: DealAdapter = DealAdapter(this)

    fun getSize(): Int {
        return DealManager.instance!!.posts.size
    }

    fun getTitle(index: Int): String {
        return DealManager.instance!!.posts[index].title
    }

    fun refresh(onComplete: () -> Unit) {
        DealManager.instance!!.updatePosts(object : PostRefreshListener {
            override fun onComplete(data: List<Post>) {
                dealAdapter.notifyDataSetChanged()
                onComplete()
            }
        })
    }
}