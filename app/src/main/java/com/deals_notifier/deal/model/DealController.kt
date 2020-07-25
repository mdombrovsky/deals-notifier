package com.deals_notifier.deal.model

import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.post.model.Post
import com.deals_notifier.scraper.model.RedditScraper
import com.deals_notifier.scraper.model.Scraper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DealController(private val dealAdapter: DealAdapter) {

    private val scraper: Scraper = RedditScraper("bapcsalescanada")

    private lateinit var posts: List<Post>


    init {

        dealAdapter.controller = this

        CoroutineScope(IO).launch {
            posts = scraper.getPosts()

            withContext(Main) {
                dealAdapter.notifyDataSetChanged()
            }

        }
    }

    fun getSize(): Int {
        return if (this::posts.isInitialized) posts.size else 0
    }

    fun getTitle(index: Int): String {
        return posts[index].title
    }
}