package com.deals_notifier.deal.controller

import com.deals_notifier.deal.ui.DealAdapter
import com.deals_notifier.post.model.Post
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.scraper.model.RedditScraper
import com.deals_notifier.scraper.model.ScraperHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DealController(private val queryHolder: QueryHolder) {

    val dealAdapter: DealAdapter = DealAdapter(this)
    private val scraperHolder: ScraperHolder = ScraperHolder()

    private lateinit var posts: List<Post>

    init {
        scraperHolder.scrapers.add(RedditScraper("bapcsalescanada"))

        CoroutineScope(IO).launch {
            refresh()
        }
    }

    fun getSize(): Int {
        return if (this::posts.isInitialized) posts.size else 0
    }

    fun getTitle(index: Int): String {
        return posts[index].title
    }

    suspend fun refresh() {
        updatePosts()
        withContext(Main) {
            dealAdapter.notifyDataSetChanged()
        }
    }

    private suspend fun updatePosts() {
        val newPosts = scraperHolder.getPosts()
        val validPosts = ArrayList<Post>()
        for (post: Post in newPosts) {
            if (queryHolder.matches(post)) {
                validPosts.add(post)
            }
        }
        this.posts = validPosts
    }
}