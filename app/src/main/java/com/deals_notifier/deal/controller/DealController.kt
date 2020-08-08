package com.deals_notifier.deal.controller

import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.post.model.Post
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.scraper.model.RedditScraper
import com.deals_notifier.deal.model.ValidDealHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DealController(private val validDealHolder: ValidDealHolder ) {

    val dealAdapter: DealAdapter = DealAdapter(this)

    fun getSize(): Int {
        return validDealHolder.posts.size
    }

    fun getTitle(index: Int): String {
        return validDealHolder.posts[index].title
    }

    suspend fun refresh() {
        updatePosts()
        withContext(Main) {
            dealAdapter.notifyDataSetChanged()
        }
    }

    private suspend fun updatePosts() {
        validDealHolder.updatePosts()
    }
}