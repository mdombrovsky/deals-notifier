package com.deals_notifier.deal.controller

import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.deal.model.DealService
import com.deals_notifier.post.model.Post
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext

class DealController() {

    val dealAdapter: DealAdapter = DealAdapter(this)

    fun getSize(): Int {
        return DealService.dealManager!!.posts.size
    }

    fun getTitle(index: Int): String {
        return DealService.dealManager!!.posts[index].title
    }

    suspend fun refresh(): List<Post> {
        val newPosts: List<Post> = DealService.dealManager!!.updatePosts()

        withContext(Main) {
            dealAdapter.notifyDataSetChanged()
        }

        return newPosts
    }
}