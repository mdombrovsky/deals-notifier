package com.deals_notifier.deal.controller

import com.deals_notifier.deal.model.ValidDealHolder
import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.post.model.Post
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext

class DealController(private val validDealHolder: ValidDealHolder) {

    val dealAdapter: DealAdapter = DealAdapter(this)

    fun getSize(): Int {
        return validDealHolder.posts.size
    }

    fun getTitle(index: Int): String {
        return validDealHolder.posts[index].title
    }

    suspend fun refresh(): List<Post> {
        val newPosts: List<Post> = validDealHolder.updatePosts()

        withContext(Main) {
            dealAdapter.notifyDataSetChanged()
        }

        return newPosts
    }

}